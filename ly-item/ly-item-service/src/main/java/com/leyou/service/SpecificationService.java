package com.leyou.service;

import com.leyou.domain.SpecGroup;
import com.leyou.domain.SpecParam;

import java.util.List;

public interface SpecificationService {

    public SpecGroup persistSpecGroup( SpecGroup specGroup );

    public List<SpecGroup> getSpecGroup(long cid);

    public List<SpecParam> queryParamByList(Long gid, Long cid, Boolean searching);
}
