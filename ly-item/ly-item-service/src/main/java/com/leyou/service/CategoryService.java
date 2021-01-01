package com.leyou.service;

import com.leyou.common.vo.PageResult;
import com.leyou.domain.Category;

import java.util.List;

public interface CategoryService {

    public List<Category> queryCategoryByPid(long pid);

    public List<Category> queryLast();

    public List<String> getCategoryNameByCategoryIds(List<Long> ids);

    public List<Category> getCategoryByCategoryIds(List<Long> ids);

    public PageResult<Category> searchCategory(String key, boolean descending, int page, int rowsPerPage, String sortBy, long parentID);

    public List<Category> getCategories();

    public List<Category> getCategoriesByParentID(long parentID);

    public Category persistCategory(Category category);
}
