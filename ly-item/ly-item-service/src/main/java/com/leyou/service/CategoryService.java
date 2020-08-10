package com.leyou.service;

import com.leyou.domain.Category;

import java.util.List;

public interface CategoryService {

    public List<Category> queryCategoryByPid(long pid);

    public List<Category> queryLast();

    public List<String> getCategoryNameByCategoryIds(List<Long> ids);

    public List<Category> getCategoryByCategoryIds(List<Long> ids);
}
