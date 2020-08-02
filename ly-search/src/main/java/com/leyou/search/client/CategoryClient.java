package com.leyou.search.client;

import com.leyou.api.CategoryAPI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//spring:
//    main:
//        allow-bean-definition-overriding: true
//allow-bean-definition-overriding: true在SpringBoot 2.1之前，这个配置默认就是true
//在2.1后配置默认就是false,如value值相同（value = "item-service"），会报错
//解决1.设为false,2.或者用contextId
@FeignClient(value = "item-service" , contextId="CategoryClient")
public interface CategoryClient extends CategoryAPI {

//    @GetMapping("category/getCategoryNameByCategoryIds")
//    public List<String> getCategoryNameByCategoryIds(
//            @RequestParam( name = "ids" , required = true ) List<Long> ids );

}
