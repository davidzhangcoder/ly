package com.leyou.service.impl;

import com.leyou.dao.SpecGroupDao;
import com.leyou.dao.SpecParamDao;
import com.leyou.domain.Category;
import com.leyou.domain.SpecGroup;
import com.leyou.domain.SpecParam;
import com.leyou.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service( value = "SpecificationServiceImpl" )
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecGroupDao specificationDao;

    @Autowired
    private SpecParamDao specParamDao;

    @Override
    public SpecGroup persistSpecGroup(SpecGroup specGroup) {
        return null;
    }

    @Override
    public List<SpecGroup> getSpecGroup(long cid) {
        List<SpecGroup> byCategory_id = specificationDao.findByCategory_Id(cid);
        for (SpecGroup specGroup : byCategory_id) {
            specGroup.getCategory();
        }
        return byCategory_id;
    }

    @Override
    public List<SpecParam> queryParamByList(Long gid, Long cid, Boolean searching) {
        //直接使用匿名内部类实现接口
        Specification specification = new Specification<SpecParam>() {
            @Override
            public Predicate toPredicate(Root<SpecParam> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                if ( gid != null ) {
                    Predicate specGroupIDPredicate = criteriaBuilder.equal(root.get("specGroupID").as(Long.class), gid);
                    predicateList.add(specGroupIDPredicate);
                }

                if ( cid != null ) {
                    Predicate categoryPredicate = criteriaBuilder.equal(root.get("cid").as(Long.class), cid);
                    predicateList.add( categoryPredicate );
                }

                if ( searching != null ) {
                    Predicate searchingPredicate = criteriaBuilder.equal(root.get("searching").as(Boolean.class), searching);
                    predicateList.add(searchingPredicate);
                }

                Predicate[] pre = new Predicate[predicateList.size()];
                pre = predicateList.toArray(pre);
                return criteriaQuery.where(pre).getRestriction();
            }
        };

        List<SpecParam> all = specParamDao.findAll(specification);

        return all;
    }
}

//2020-09-20 02:48:15.876  WARN 1746 --- [nio-8081-exec-5] .w.s.m.s.DefaultHandlerExceptionResolver :
//        Resolved [org.springframework.http.converter.HttpMessageNotWritableException:
//        Could not write JSON: could not initialize proxy [com.leyou.domain.Category#76] - no Session;
//nested exception is com.fasterxml.jackson.databind.JsonMappingException:
//        could not initialize proxy [com.leyou.domain.Category#76] - no Session (through reference chain: java.util.ArrayList[0]->com.leyou.domain.SpecParam["category"]->com.leyou.domain.Category$HibernateProxy$uq1yjDLP["name"])]
