package com.leyou.service.impl;

import com.leyou.common.vo.PageResult;
import com.leyou.dao.CategoryDao;
import com.leyou.domain.Brand;
import com.leyou.domain.Category;
import com.leyou.service.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "CategoryServiceImpl")
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

    @Override
    public List<String> getCategoryNameByCategoryIds(List<Long> ids) {

        //直接使用匿名内部类实现接口
        Specification specification = new Specification<Category>() {
            @Override
            public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                if (!CollectionUtils.isEmpty(ids)) {
                    predicateList.add(root.get("id").in(ids));
                }

                // testing URL: http://api.leyou.com/api/item-service/category/getCategoryNameByCategoryIds?ids=1,2,3,241
//                Predicate namePredicate = criteriaBuilder.like( root.get("name").as(String.class), "M%" );
//                predicateList.add(namePredicate);

                // generated SQL
//                Hibernate:
//                select
//                category0_.id as id1_1_,
//                        category0_.is_parent as is_paren2_1_,
//                category0_.name as name3_1_,
//                        category0_.parent_id as parent_i4_1_,
//                category0_.sort as sort5_1_
//                        from
//                tb_category category0_
//                where
//                        (
//                                category0_.id in (
//                                1 , 2 , 3 , 241
//                                )
//                        )
//                and (
//                        category0_.name like ?
//                )


                Predicate[] pre = new Predicate[predicateList.size()];
                pre = predicateList.toArray(pre);
                return criteriaQuery.where(pre).getRestriction();
            }
        };

        List<Category> all = categoryDao.findAll(specification);
        List<String> resultList = all.stream().map(a -> a.getName()).collect(Collectors.toList());

        return resultList;
    }

    @Override
    public List<Category> getCategoryByCategoryIds(List<Long> ids) {
        return categoryDao.findAllById(ids);
    }

    @Override
    public PageResult<Category> searchCategory(String key, boolean descending, int page, int rowsPerPage, String sortBy, long parentID) {
        page--;
        page = page < 0 ? 0 : page;//page 为页码，数据库从0页开始
        //可以使用重载的 of(int page, int size, Sort sort) 方法指定排序字段
        //Pageable pageable = PageRequest.of((int)page, (int)rowsPerPage);

        // Order定义了排序规则。参数一是根据倒叙还是正序，参数二是根据那个字段进行排序。
        Sort.Order order = new Sort.Order((descending?Sort.Direction.DESC:Sort.Direction.ASC), sortBy);
        // Sort对象封装了排序的规则。
        Sort sort = Sort.by(order);
        Pageable pageable = PageRequest.of(page, rowsPerPage , sort );


        // Specification specification = (a,b,c) ->{ return null; };

        //直接使用匿名内部类实现接口
        Specification specification = new Specification<Category>() {
            @Override
            public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                //条件1：查询 tvName 为 海信 的数据，root.get 中的值与 TV 实体中的属性名称对应
                if (!StringUtils.isBlank(key)) {
                    predicateList.add( cb.like( root.get("name").as(String.class), "%"+key+"%" ) );
                }

                predicateList.add( cb.equal(root.get("parentID").as(Integer.class), parentID) );

                //条件2：TV 生产日期（dateOfProduction）大于等于 start 的数据，root.get 中的 dateOfProduction 必须对应 TV 中的属性
                //predicateList.add(cb.greaterThanOrEqualTo(root.get("dateOfProduction").as(Date.class), start));

                //条件3：TV 生产日期（dateOfProduction）小于等于 end
                //predicateList.add(cb.lessThanOrEqualTo(root.get("dateOfProduction").as(Date.class), end));

                Predicate[] pre = new Predicate[predicateList.size()];
                pre = predicateList.toArray(pre);
                return query.where(pre).getRestriction();
            }
        };

        Page<Category> categoryPage = categoryDao.findAll(specification , pageable);//没有数据时，返回空列表

//        if(CollectionUtils.isEmpty(brandPage.getContent())) {
//            throw new LyException(ExceptionEnum.BRAND_SEARCH_LIST_IS_EMPTY);
//        }

        PageResult<Category> pageResult = new PageResult(categoryPage.getTotalElements(), (long)categoryPage.getTotalPages(), categoryPage.getContent());

        return pageResult;
    }

    @Override
    public List<Category> getCategories() {
        return categoryDao.findAll();
    }

    @Override
    public List<Category> getCategoriesByParentID(long parentID) {
        return categoryDao.findByParentID(parentID);
    }

    @Override
    public Category persistCategory(Category category) {
        category = categoryDao.save(category);
        if(category.getParentID()!=0) {
            categoryDao.findById(category.getParentID()).ifPresent( parentCategory -> {
                if( !parentCategory.isParent() ) {
                    parentCategory.setParent(true);
                    categoryDao.save(parentCategory);
                }
            });
        }
        return category;
    }

}
