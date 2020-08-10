package com.leyou.api;

import com.leyou.domain.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/category")
public interface CategoryAPI {

    @GetMapping("getCategoryNameByCategoryIds")
    public List<String> getCategoryNameByCategoryIds(
            @RequestParam( name = "ids" , required = true ) List<Long> ids );

    @GetMapping("getCategoryByCategoryIds")
    public List<Category> getCategoryByCategoryIds(
            @RequestParam( name = "ids" , required = true ) List<Long> ids );

}
