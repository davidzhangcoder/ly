package com.leyou.api;

import com.leyou.domain.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/brand")
public interface BrandAPI {

    @RequestMapping(value = "{brandId}" , method = { RequestMethod.GET } )
    public String getBrandNameById(@PathVariable long brandId);

}
