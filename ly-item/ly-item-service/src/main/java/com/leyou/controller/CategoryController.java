package com.leyou.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.domain.Category;
import com.leyou.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("getCategoryByCategoryIds")
    public ResponseEntity<List<Category>> getCategoryByCategoryIds(
            @RequestParam( name = "ids" , required = true ) List<Long> ids ) {
        List<Category> resultList = categoryService.getCategoryByCategoryIds(ids);
        return ResponseEntity.ok( resultList );
    }

    @GetMapping("getCategoriesByPage")
    public ResponseEntity<PageResult<Category>> getCategoriesByPage(
            @RequestParam( name = "key" , required = true ) String key,
            @RequestParam( name = "descending" , required = true ) boolean descending,
            @RequestParam( name = "page" , required = true ) int page,
            @RequestParam( name = "rowsPerPage" , required = true ) int rowsPerPage,
            @RequestParam( name = "sortBy" , required = true ) String sortBy,
            @RequestParam( name = "parentID" , required = true ) long parentID
    ) {
        return ResponseEntity.ok(categoryService.searchCategory(key, descending, page, rowsPerPage, sortBy, parentID));
    }

    @GetMapping("getCategoriesByParentID")
    public ResponseEntity<List<Category>> getCategoriesByParentID(@RequestParam( name = "parentID" , required = true ) long parentID) {
        return ResponseEntity.ok(categoryService.getCategoriesByParentID(parentID));
    }

    @RequestMapping(value = "persistCategory" , method = { RequestMethod.POST } )
    public ResponseEntity<Category> persistBrand(@RequestBody Category category) {
        return ResponseEntity.ok( categoryService.persistCategory(category) );
    }

}
