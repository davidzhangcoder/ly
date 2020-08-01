package com.leyou.service.impl;

import com.leyou.dao.SpecificationDao;
import com.leyou.domain.SpecGroup;
import com.leyou.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service( value = "SpecificationServiceImpl" )
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecificationDao specificationDao;

    @Override
    public SpecGroup persistSpecGroup(SpecGroup specGroup) {
        return null;
    }

    @Override
    public List<SpecGroup> getSpecGroup(long cid) {
        List<SpecGroup> byCategory_id = specificationDao.findByCategory_Id(cid);
        for (SpecGroup specGroup : byCategory_id) {
//            specGroup.getCategory();
        }
        return byCategory_id;
    }
}
