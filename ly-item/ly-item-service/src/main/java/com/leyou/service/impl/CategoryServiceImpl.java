package com.leyou.service.impl;

import com.leyou.dao.CategoryDao;
import com.leyou.domain.Category;
import com.leyou.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service( value = "CategoryServiceImpl" )
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<Category> queryCategoryByPid(long pid) {
        return categoryDao.findByParentID(pid);
    }

    @Override
    public List<Category> queryLast() {
        return categoryDao.queryLast();
    }
}
