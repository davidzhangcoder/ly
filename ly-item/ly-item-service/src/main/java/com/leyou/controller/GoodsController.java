package com.leyou.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.domain.Sku;
import com.leyou.domain.Spu;
import com.leyou.domain.SpuDetail;
import com.leyou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/goods")
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("getSKUBySPUId")
    public ResponseEntity<List<Sku>> getSKUBySPUId(@RequestParam( name = "spuid" , required = true ) long spuid) {
        List<Sku> skuBySPUIdList = goodsService.getSKUBySPUId(spuid);
        return ResponseEntity.ok( skuBySPUIdList );
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

}
