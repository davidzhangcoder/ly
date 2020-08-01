package com.leyou.service;

import com.leyou.domain.SpecGroup;

import java.util.List;

public interface SpecificationService {

    public SpecGroup persistSpecGroup( SpecGroup specGroup );

    public List<SpecGroup> getSpecGroup(long cid);
}
