package com.leyou.controller;

import com.leyou.common.dto.CartDto;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.vo.PageResult;
import com.leyou.domain.Sku;
import com.leyou.domain.Spu;
import com.leyou.domain.SpuDetail;
import com.leyou.service.GoodsService;
import com.leyou.service.GoodsServiceHystrix;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/goods")
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsServiceHystrix goodsServiceHystrix;

    @GetMapping("getOnSaleProduct")
    public ResponseEntity<List<Sku>> getOnSaleProduct() {
        //Test Code
        List<Long> skuIds = Arrays.asList(10781492357l , 26266538223l);

        List<Sku> onSaleProductList = goodsService.getOnSaleProduct(skuIds);
        //System.out.println(JsonUtils.serialize(onSaleProductList));
        //[{"id":10781492357,"spu":{"id":81,"brandId":{"id":8557,"name":"华为（HUAWEI）","image":"http://img10.360buyimg.com/popshop/jfs/t5662/36/8888655583/7806/1c629c01/598033b4Nd6055897.jpg","letter":"H","categories":[{"id":76,"name":"手机","parentID":75,"sort":1,"parent":false}]},"category1":{"id":74,"name":"手机","parentID":0,"sort":2,"parent":true},"category2":{"id":75,"name":"手机通讯","parentID":74,"sort":1,"parent":true},"category3":{"id":76,"name":"手机","parentID":75,"sort":1,"parent":false},"title":"华为（HUAWEI） nova 智能手机 4G手机 ","subTitle":"支持京东配送到付 送实用好礼 今日下单默认送延保一年 ，美颜自拍！<a href=\"https://item.jd.com/26509792446.html\" target=\"_blank\">华为nova3E热卖中</a>","saleable":true,"valid":true,"createTime":1524340668000,"lastUpdateTime":1524340668000},"spuId":81,"title":"华为（HUAWEI） nova 智能手机 4G手机 玫瑰金 (4G+64G)高配","images":"http://image.leyou.com/images/7/14/1524297466806.jpg","price":109900,"ownSpec":"{\"4\":\"玫瑰金\",\"12\":\"4GB\",\"13\":\"64GB\"}","indexes":"0_0_0","enable":true,"createTime":1524340667000,"lastUpdateTime":1524340667000,"stock":null},{"id":26266538223,"spu":{"id":142,"brandId":{"id":18374,"name":"小米（MI）","image":"http://img10.360buyimg.com/popshop/jfs/t7084/169/439244907/4647/724c7958/598042c9N6e4e79e5.jpg","letter":"X","categories":[{"id":76,"name":"手机","parentID":75,"sort":1,"parent":false},{"id":84,"name":"移动电源","parentID":83,"sort":1,"parent":false},{"id":86,"name":"蓝牙耳机","parentID":83,"sort":3,"parent":false},{"id":94,"name":"手机保护套","parentID":83,"sort":11,"parent":false},{"id":105,"name":"平板电视","parentID":104,"sort":1,"parent":false},{"id":117,"name":"电视盒子","parentID":104,"sort":13,"parent":false},{"id":128,"name":"电饭煲","parentID":125,"sort":3,"parent":false},{"id":324,"name":"笔记本","parentID":323,"sort":1,"parent":false},{"id":326,"name":"游戏本","parentID":323,"sort":3,"parent":false},{"id":327,"name":"平板电脑","parentID":323,"sort":4,"parent":false}]},"category1":{"id":74,"name":"手机","parentID":0,"sort":2,"parent":true},"category2":{"id":75,"name":"手机通讯","parentID":74,"sort":1,"parent":true},"category3":{"id":76,"name":"手机","parentID":75,"sort":1,"parent":false},"title":"小米（MI） 红米note5 手机 ","subTitle":"【赠多重好礼】 游戏手机 王者荣耀 吃鸡 AI双摄逆光暗光更出色/<a href=\"https://item.jd.com/21676464002.html\" target=\"_blank\">红米5plus推广中</a>","saleable":true,"valid":true,"createTime":1524340802000,"lastUpdateTime":1524340802000},"spuId":142,"title":"小米（MI） 红米note5 手机 黑色 全网版(4GB+64GB )","images":"http://image.leyou.com/images/11/5/1524297600169.jpg","price":134800,"ownSpec":"{\"4\":\"黑色\",\"12\":\"4GB\",\"13\":\"64GB\"}","indexes":"0_0_0","enable":true,"createTime":1524340800000,"lastUpdateTime":1524340800000,"stock":null}
        //[{"id":10781492357,"spu":{"id":81,"brandId":{"id":8557,"name":"华为（HUAWEI）","image":"http://img10.360buyimg.com/popshop/jfs/t5662/36/8888655583/7806/1c629c01/598033b4Nd6055897.jpg","letter":"H","categories":[{"id":76,"name":"手机","parentID":75,"sort":1,"parent":false}]},"category1":{"id":74,"name":"手机","parentID":0,"sort":2,"parent":true},"category2":{"id":75,"name":"手机通讯","parentID":74,"sort":1,"parent":true},"category3":{"id":76,"name":"手机","parentID":75,"sort":1,"parent":false},"title":"华为（HUAWEI） nova 智能手机 4G手机 ","subTitle":"支持京东配送到付 送实用好礼 今日下单默认送延保一年 ，美颜自拍！","saleable":true,"valid":true,"createTime":1524340668000,"lastUpdateTime":1524340668000},"spuId":81,"title":"华为（HUAWEI） nova 智能手机 4G手机 玫瑰金 (4G+64G)高配","images":"http://image.leyou.com/images/7/14/1524297466806.jpg","price":109900,"ownSpec":"{\"4\":\"玫瑰金\",\"12\":\"4GB\",\"13\":\"64GB\"}","indexes":"0_0_0","enable":true,"createTime":1524340667000,"lastUpdateTime":1524340667000,"stock":null}]
        return ResponseEntity.ok(onSaleProductList);
    }

    @GetMapping("getSKUBySPUId")
    public ResponseEntity<List<Sku>> getSKUBySPUId(@RequestParam( name = "spuid" , required = true ) long spuid) {
        List<Sku> skuBySPUIdList = goodsService.getSKUBySPUId(spuid);
        return ResponseEntity.ok( skuBySPUIdList );
    }

    @GetMapping("getSKUListByIds")
    public ResponseEntity<List<Sku>> getSKUListByIds(@RequestParam( name = "ids" , required = true ) List<Long> skuIds) {
        List<Sku> skuList = goodsService.getSKUListByIds(skuIds);
        return  ResponseEntity.ok(skuList);
    }

    @GetMapping("getSPUDetailBySPUId")
    public ResponseEntity<SpuDetail> getSPUDetailBySPUId(@RequestParam( name = "spuid" , required = true ) long spuid ) {
        SpuDetail spuDetail = goodsService.getSPUDetailBySPUId(spuid);
        return ResponseEntity.ok( spuDetail );
    }

    @GetMapping("getSpuByPage")
    public ResponseEntity<PageResult<Spu>> getSpuByPage(
            @RequestParam( name = "descending" , required = true ) boolean descending,
            @RequestParam( name = "page" , required = true ) int page,
            @RequestParam( name = "rowsPerPage" , required = true ) int rowsPerPage,
            @RequestParam( name = "sortBy" , required = true ) String sortBy
    ) {
        return ResponseEntity.ok(goodsService.getSpuByPage(descending, page, rowsPerPage, sortBy));
    }

    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
        Spu spu = this.goodsService.querySpuById(id);
        if(spu == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(spu);
    }

    /**
     * 减少库存
     * @param cartDtos
     * @return
     */
    @PostMapping("stock/decrease")
    public ResponseEntity<Void> decreaseStock(@RequestBody List<CartDto> cartDtos){
        goodsService.decreaseStock(cartDtos);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value="getStockBySkuId/{skuID}")
    public long getStockBySkuId(@PathVariable("skuID") long skuID){
        return goodsService.getStockBySkuId(skuID);
    }

    @GetMapping(value="testFallBack/{id}")
    public void testFallBack(@PathVariable("id") long id) {
        System.out.println("testFallBack");

        goodsServiceHystrix.testFallBack(id);
    }

}
