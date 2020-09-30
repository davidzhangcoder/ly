package com.leyou.controller;

import com.leyou.common.dto.CartDto;
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

import java.util.List;

@RequestMapping("/goods")
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsServiceHystrix goodsServiceHystrix;

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

    @GetMapping(value="testFallBack/{id}")
    public void testFallBack(@PathVariable("id") long id) {
        System.out.println("testFallBack");

        goodsServiceHystrix.testFallBack(id);
    }

}
