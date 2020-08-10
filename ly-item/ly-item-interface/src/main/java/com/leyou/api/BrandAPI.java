package com.leyou.api;

import com.leyou.domain.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/brand")
public interface BrandAPI {

    @RequestMapping(value = "{brandId}" , method = { RequestMethod.GET } )
    public String getBrandNameById(@PathVariable long brandId);

    @RequestMapping(value = "getBrandsByIds" , method = { RequestMethod.GET } )
    public List<Brand> getBrandsByIds(@RequestParam( name = "ids" , required = true ) List<Long> ids );

}
