package com.leyou.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.domain.Brand;
import com.leyou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/brand")
@RestController
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("testHttpStatus")
    public ResponseEntity<String> testHttpStatus() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

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

    @RequestMapping(value = "deleteBrand/{brandId}" , method = { RequestMethod.DELETE } )
    public ResponseEntity<Void> deleteBrand(@PathVariable long brandId) {
        brandService.deleteBrand(brandId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "{brandId}" , method = { RequestMethod.GET } )
    public ResponseEntity<String> getBrandNameById(@PathVariable long brandId) {
        Brand brand = brandService.getBrandById( brandId );
        String brandName = "";
        if( brand != null )
            brandName = brand.getName();
        return ResponseEntity.ok( brandName );
    }

    @RequestMapping(value = "getBrandsByIds" , method = { RequestMethod.GET } )
    public ResponseEntity<List<Brand>> getBrandsByIds( @RequestParam( name = "ids" , required = true ) List<Long> ids ){
        List<Brand> brandsByIds = brandService.getBrandsByIds(ids);
        return ResponseEntity.ok( brandsByIds );
    }

}
