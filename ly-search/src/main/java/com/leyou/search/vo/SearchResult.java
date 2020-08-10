package com.leyou.search.vo;

import com.leyou.common.vo.PageResult;
import com.leyou.domain.Brand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResult<T> extends PageResult<T> {

    private List<Brand> brandList = new ArrayList<Brand>();

    private Map<Long, String> categoryNameMap = new HashMap<Long, String>();

    private Map<String, List<String>> specListMap;

    public SearchResult(List<Brand> brandList, Map<Long, String> categoryNameMap, Map<String, List<String>> specListMap) {
        this.brandList = brandList;
        this.categoryNameMap = categoryNameMap;
        this.specListMap = specListMap;
    }

    public SearchResult(Long total, List<T> items, List<Brand> brandList, Map<Long, String> categoryNameMap, Map<String, List<String>> specListMap) {
        super(total, items);
        this.brandList = brandList;
        this.categoryNameMap = categoryNameMap;
        this.specListMap = specListMap;
    }

    public SearchResult(Long total, Long totalPage, List<T> items, List<Brand> brandList, Map<Long, String> categoryNameMap, Map<String, List<String>> specListMap) {
        super(total, totalPage, items);
        this.brandList = brandList;
        this.categoryNameMap = categoryNameMap;
        this.specListMap = specListMap;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

    public Map<Long, String> getCategoryNameMap() {
        return categoryNameMap;
    }

    public void setCategoryNameMap(Map<Long, String> categoryNameMap) {
        this.categoryNameMap = categoryNameMap;
    }

    public Map<String, List<String>> getSpecListMap() {
        return specListMap;
    }

    public void setSpecListMap(Map<String, List<String>> specListMap) {
        this.specListMap = specListMap;
    }
}
