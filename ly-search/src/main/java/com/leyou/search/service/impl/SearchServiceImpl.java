package com.leyou.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.vo.PageResult;
import com.leyou.domain.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import com.leyou.search.vo.SearchRequest;
import com.leyou.search.vo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.UnmappedTerms;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service( value = "SearchServiceImpl" )
@Transactional
public class SearchServiceImpl implements SearchService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public Goods buildGoods(Spu spu) throws IOException {
        Goods goods = new Goods();
        // 查询商品分类名称
        List<String> names =
                this.categoryClient.getCategoryNameByCategoryIds(Arrays.asList(spu.getCategory1().getId(), spu.getCategory2().getId(),
                        spu.getCategory3().getId()));
        // 查询sku
        List<Sku> skus = this.goodsClient.getSKUBySPUId(spu.getId());
        // 查询详情
        SpuDetail spuDetail = this.goodsClient.getSPUDetailBySPUId(spu.getId()); // 查询规格参数
        List<SpecParam> params = this.specificationClient.getSpecParamByList(null, spu.getCategory3().getId(), true );
        // 处理sku,仅封装id、价格、标题、图片,并获得价格集合
        List<Long> prices = new ArrayList<>();
        List<Map<String, Object>> skuList = new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("price", sku.getPrice());
            skuMap.put("image", StringUtils.isBlank(sku.getImages()) ? "" :
                    StringUtils.split(sku.getImages(), ",")[0]);
            skuList.add(skuMap);
        });
        // 处理规格参数
        Map<String, Object> genericSpecs =
                mapper.readValue(spuDetail.getSpecifications(), new TypeReference<Map<String,
                        Object>>() {
                });
        Map<String, Object> specialSpecs =
                mapper.readValue(spuDetail.getSpecTemplate(), new TypeReference<Map<String,
                        Object>>() {
                });
        // 获取可搜索的规格参数
        Map<String, Object> searchSpec = new HashMap<>();
        // 过滤规格模板,把所有可搜索的信息保存到Map中
        Map<String, Object> specMap = new HashMap<>();
        params.forEach(p -> {
            if (p.getSearching()) {
                if (p.getGeneric()) {
                    String value =
                            genericSpecs.get(p.getId().toString()).toString();
                    if (p.getNumeric()) {
                        value = chooseSegment(value, p);
                    }
                    specMap.put(p.getName(), StringUtils.isBlank(value) ? "其它" : value);
                } else {
                    specMap.put(p.getName(),
                            specialSpecs.get(p.getId().toString()));
                }
            }
        });
        goods.setId(spu.getId());
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId().getId());
        goods.setCid1(spu.getCategory1().getId());
        goods.setCid2(spu.getCategory2().getId());
        goods.setCid3(spu.getCategory3().getId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setAll(spu.getTitle() + " " + StringUtils.join(names, " "));
        goods.setPrice(prices);
        goods.setSkus(mapper.writeValueAsString(skuList));
        goods.setSpecs(specMap);
        return goods;
    }

    @Override
    public SearchResult<Goods> searchGoods(SearchRequest searchRequest) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        BoolQueryBuilder baseQuery = buildBaseQuery(searchRequest);

        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(new String[]{"id", "price" ,"skus", "subTitle"}, null);

        ScoreSortBuilder scoreSortBuilder = SortBuilders.scoreSort();

        PageRequest pageRequest = PageRequest.of((int) searchRequest.getPage(), (int) searchRequest.getSize());

        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.withQuery(baseQuery)
                .withSourceFilter( fetchSourceFilter )
                .withSort(scoreSortBuilder)
                .withPageable(pageRequest)
                .build();

        //聚合
        //BrandID聚合
        TermsAggregationBuilder buildIdAggrationField = AggregationBuilders
                .terms(Goods.Aggration.AGGRATION_BRANDID.getName())
                .field(Goods.Properties.BRANDID.getName());
        nativeSearchQuery.addAggregation(buildIdAggrationField);

        //CATEGORYID3聚合
        TermsAggregationBuilder categoryId3AggregationField = AggregationBuilders
                .terms(Goods.Aggration.AGGRATION_CATEGORYID3.getName())
                .field(Goods.Properties.CATEGORYID3.getName());
        nativeSearchQuery.addAggregation(categoryId3AggregationField);

        //spec聚合
        List<SpecParam> specParamByList = specificationClient.getSpecParamByList(null, searchRequest.getCategoryid3(), true);
        for (SpecParam specParam : specParamByList) {
            if( specParam.getSearching() ){
                TermsAggregationBuilder agg = AggregationBuilders
                        .terms( Goods.Aggration.AGGRATION_SPEC.getName() + "_" + Goods.Properties.SPECS.getName() + "." + specParam.getName() )
                        .field(Goods.Properties.SPECS.getName() + "." + specParam.getName() + ".keyword");
                nativeSearchQuery.addAggregation( agg );
            }
        }


        AggregatedPage<Goods> searchResult = (AggregatedPage<Goods>)goodsRepository.search(nativeSearchQuery);


        //打印ElasticSearch QSL
        System.out.println( nativeSearchQuery.getQuery().toString() );
//        System.out.println(nativeSearchQueryBuilder.build().getQuery().toString());

        //解析聚合的结果
        //解析BrandID聚合
        List<Long> brandIDList = new ArrayList<Long>();
        LongTerms brandIDLongTerms = (LongTerms)searchResult.getAggregation(Goods.Aggration.AGGRATION_BRANDID.getName());
        List<LongTerms.Bucket> brandIDBuckets = brandIDLongTerms.getBuckets();
        for (LongTerms.Bucket brandIDBucket : brandIDBuckets) {
            long id = brandIDBucket.getKeyAsNumber().longValue();
            brandIDList.add( id );
        }
        //List<String> brandNameList = brandClient.getBrandsByIds(brandIDList).stream().map(a -> a.getName()).collect(Collectors.toList());
        List<Brand> brandList = brandClient.getBrandsByIds(brandIDList).stream().collect(Collectors.toList());

        //解析CATEGORYID3聚合
        List<Long> categoryID3List = new ArrayList<Long>();
        LongTerms categoryID3LongTerms = (LongTerms)searchResult.getAggregation(Goods.Aggration.AGGRATION_CATEGORYID3.getName());
        List<LongTerms.Bucket> categoryID3Buckets = categoryID3LongTerms.getBuckets();
        for (LongTerms.Bucket categoryIDBucket : categoryID3Buckets) {
            long id = categoryIDBucket.getKeyAsNumber().longValue();
            categoryID3List.add( id );
        }
        //List<String> categoryNameList = categoryClient.getCategoryNameByCategoryIds(categoryID3List);
        Map<Long, String> categoryNameMap = categoryClient.getCategoryByCategoryIds(categoryID3List).stream()
                .sorted( (a,b) -> Long.valueOf(a.getId()).compareTo(Long.valueOf(b.getId())) )
                .collect(Collectors.toMap(a -> a.getId(), a -> a.getName()));

        //解析spec聚合
        //Map<String, List<String>> specListMap = buildSpecAggration(allMatchQueryBuilder, searchRequest.getCategoryid3(), searchRequest);
        Map<String, List<String>> specListMap = new HashMap<String, List<String>>();
        for (SpecParam specParam : specParamByList) {
            if( specParam.getSearching() ) {
                StringTerms stringTerms = (StringTerms)searchResult.getAggregation(
                        Goods.Aggration.AGGRATION_SPEC.getName() + "_" + Goods.Properties.SPECS.getName() + "." + specParam.getName());
                List<String> valueList = new ArrayList<String>();
                for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                    valueList.add( bucket.getKeyAsString() );
                }
                specListMap.put( specParam.getName() , valueList );
            }
        }



        //组装PageResult
        SearchResult<Goods> pageSearchResult = new SearchResult(
                searchResult.getTotalElements() ,
                (long)searchResult.getTotalPages() ,
                searchResult.getContent() ,
                brandList ,
                categoryNameMap,
                specListMap );

        return pageSearchResult;
    }

    private BoolQueryBuilder buildBaseQuery(SearchRequest searchRequest) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        MatchQueryBuilder allMatchQuery = QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND);
        boolQueryBuilder.must().add( allMatchQuery );

        if( !CollectionUtils.isEmpty(searchRequest.getSpecfilters()) ) {
            for (Map.Entry<String, String> stringStringEntry : searchRequest.getSpecfilters().entrySet()) {
                String key = stringStringEntry.getKey();
                String value = stringStringEntry.getValue();
                if( key.equalsIgnoreCase( Goods.Properties.BRANDID.getName() ) ) {
                    TermQueryBuilder brandIdBuilder = QueryBuilders.termQuery(Goods.Properties.BRANDID.getName(), Long.parseLong(value));
                    boolQueryBuilder.filter().add( brandIdBuilder );
                } else {
                    TermQueryBuilder specQueryBuilder = QueryBuilders.termQuery(Goods.Properties.SPECS.getName() + "." + key + ".keyword", value);
                    boolQueryBuilder.filter().add( specQueryBuilder );
                }
            }
        }

        return boolQueryBuilder;
    }

    private Map<String,List<String>> buildSpecAggration( QueryBuilder baseQueryBuilder , Long cid3 , SearchRequest searchRequest ) {

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//        MatchQueryBuilder allMatchQueryBuilder = QueryBuilders.matchQuery("all", searchRequest.getKey());
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.withQuery(baseQueryBuilder)
                .build();

        List<SpecParam> specParamByList = specificationClient.getSpecParamByList(null, cid3, true);
        for (SpecParam specParam : specParamByList) {
            if( specParam.getSearching() ){
                TermsAggregationBuilder agg = AggregationBuilders
                        .terms( Goods.Aggration.AGGRATION_SPEC.getName() + "_" + Goods.Properties.SPECS.getName() + "." + specParam.getName() )
                        .field(Goods.Properties.SPECS.getName() + "." + specParam.getName() + ".keyword");
                nativeSearchQuery.addAggregation( agg );
            }
        }

        AggregatedPage<Goods> searchResult = (AggregatedPage<Goods>)goodsRepository.search(nativeSearchQuery);

        Map<String,List<String>> specAggResult = new HashMap<String,List<String>>();
        for (SpecParam specParam : specParamByList) {
            if( specParam.getSearching() ) {
                StringTerms stringTerms = (StringTerms)searchResult.getAggregation(
                        Goods.Aggration.AGGRATION_SPEC.getName() + "_" + Goods.Properties.SPECS.getName() + "." + specParam.getName());
                List<String> valueList = new ArrayList<String>();
                for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                    valueList.add( bucket.getKeyAsString() );
                }
                specAggResult.put( specParam.getName() , valueList );
            }
        }

        return specAggResult;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}
