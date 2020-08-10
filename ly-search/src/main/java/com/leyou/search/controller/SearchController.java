package com.leyou.search.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.domain.SpecGroup;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.SearchService;
import com.leyou.search.vo.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/search")
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping( value = "searchGoods" , method = { RequestMethod.POST } )
    public ResponseEntity<PageResult<Goods>> searchGoods(@RequestBody SearchRequest searchRequest) {
        PageResult<Goods> goodsListPageResult = searchService.searchGoods( searchRequest );
        return ResponseEntity.ok( goodsListPageResult );
    }

}
