package com.leyou.service.impl;

import com.leyou.dao.CategoryDao;
import com.leyou.domain.Brand;
import com.leyou.domain.Category;
import com.leyou.service.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
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

}
