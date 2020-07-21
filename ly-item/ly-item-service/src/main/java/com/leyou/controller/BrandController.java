package com.leyou.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.domain.Brand;
import com.leyou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/brand")
@RestController
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("getBrands")
    public ResponseEntity<PageResult<Brand>> getBrands(
            @RequestParam( name = "key" , required = true ) String key,
            @RequestParam( name = "descending" , required = true ) boolean descending,
            @RequestParam( name = "page" , required = true ) int page,
            @RequestParam( name = "rowsPerPage" , required = true ) int rowsPerPage,
            @RequestParam( name = "sortBy" , required = true ) String sortBy
    ) {
        return ResponseEntity.ok(brandService.searchBrand(key, descending, page, rowsPerPage, sortBy));
    }

    @RequestMapping(value = "persistBrand" , method = { RequestMethod.POST } )
    public ResponseEntity<Brand> persistBrand(@RequestBody Brand brand) {
        return ResponseEntity.ok( brandService.persistBrand(brand) );
    }

}
