package com.leyou.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.domain.Brand;
import com.leyou.domain.Category;
import com.leyou.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/category")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public ResponseEntity<List<Category>> getCategoryList(
            @RequestParam( name = "pid" , required = true ) long pid
    ) {

        //如果pid的值为-1那么需要获取数据库中最后一条数据
        if (pid == -1){
            List<Category> last = this.categoryService.queryLast();
            return ResponseEntity.ok(last);
        }
        else {
            List<Category> list = this.categoryService.queryCategoryByPid(pid);
            if (list == null) {
                //没有找到返回404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            //找到返回200
            return ResponseEntity.ok(list);
        }
    }

    @GetMapping("getCategoryNameByCategoryIds")
    public ResponseEntity<List<String>> getCategoryNameByCategoryIds(
            @RequestParam( name = "ids" , required = true ) List<Long> ids ) {
        List<String> resultList = categoryService.getCategoryNameByCategoryIds(ids);
        return ResponseEntity.ok( resultList );
    }

}
