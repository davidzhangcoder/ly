package com.leyou.page.service.impl;

import com.leyou.domain.*;
import com.leyou.page.client.*;
import com.leyou.page.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.transaction.Transactional;
import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "PageServiceImpl")
@Transactional
public class PageServiceImpl implements PageService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private TemplateEngine templateEngine;

    private static final Logger logger = LoggerFactory.getLogger(PageServiceImpl.class);

    @Override
    public Map<String, Object> loadModel(Long spuId) {
        try {
            // 查询spu
            Spu spu = this.goodsClient.querySpuById(spuId); // 查询spu详情
            SpuDetail spuDetail = this.goodsClient.getSPUDetailBySPUId(spuId); // 查询sku
            List<Sku> skus = this.goodsClient.getSKUBySPUId(spuId);

            // 查询品牌
            List<Brand> brands =
                    this.brandClient.getBrandsByIds(Arrays.asList(spu.getBrandId().getId()));
            // 查询分类
            List<Category> categories = getCategories(spu);

            // 查询组内参数
            List<SpecGroup> specGroups =
                    this.specificationClient.getSpecGroup(spu.getCategory3().getId());

            // 查询所有特有规格参数
            List<SpecParam> specParams =
                    this.specificationClient.getSpecParamByList(null, spu.getCategory3().getId(), null);

            // 处理规格参数
            Map<Long, String> paramMap = new HashMap<>();
            specParams.forEach(param -> {
                paramMap.put(param.getId(), param.getName());
            });
            Map<String, Object> map = new HashMap<>();
            map.put("spu", spu);
            map.put("title" , spu.getTitle());
            map.put("subTitle",spu.getSubTitle());
            map.put("detail", spuDetail);
            map.put("skus", skus);
            map.put("brand", brands.get(0));
            map.put("categories", categories);
            map.put("specs", specGroups);
            map.put("params", paramMap);

            logger.info("[静态页服务] - 加载商品数据完成,spuId:{}", spuId);
            return map;
        } catch (Exception e) {
            logger.error("加载商品数据出错,spuId:{}", spuId, e);
        }
        return null;
    }

    @Override
    public void createHtml(Long spuId) {
        // 获取页面数据
        Map<String, Object> modelMap = loadModel(spuId);

        // 创建thymeleaf上下文对象
        Context context = new Context();
        context.setVariables(modelMap);

        // 创建输出流
        File file = new File("/Users/davidzhang/Documents/dockerworkspace/ngnix1/html/" +
                spuId + ".html");
        try( Writer writer = new PrintWriter(file , "UTF-8"); ) {
            templateEngine.process("item", context, writer);
        } catch(Exception e) {
            logger.error("[生成静态页] - 生成静态页出错,spuId:{}", spuId, e);
        }
    }

    private List<Category> getCategories(Spu spu) {
        try {
            List<String> names = this.categoryClient.getCategoryNameByCategoryIds(
                    Arrays.asList(spu.getCategory1().getId(),
                            spu.getCategory2().getId(),
                            spu.getCategory3().getId()));
            Category c1 = new Category();
            c1.setName(names.get(0));
            c1.setId(spu.getCategory1().getId());
            Category c2 = new Category();
            c2.setName(names.get(1));
            c2.setId(spu.getCategory2().getId());
            Category c3 = new Category();
            c3.setName(names.get(2));
            c3.setId(spu.getCategory3().getId());
            return Arrays.asList(c1, c2, c3);
        } catch (Exception e) {
            logger.error("查询商品分类出错,spuId:{}", spu.getId(), e); }
        return null;
    }


}
