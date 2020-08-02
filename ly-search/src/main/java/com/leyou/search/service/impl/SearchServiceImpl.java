package com.leyou.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.domain.Sku;
import com.leyou.domain.SpecParam;
import com.leyou.domain.Spu;
import com.leyou.domain.SpuDetail;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service( value = "SearchServiceImpl" )
@Transactional
public class SearchServiceImpl implements SearchService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

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
