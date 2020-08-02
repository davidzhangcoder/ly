package com.leyou.dao;

import com.leyou.domain.SpecParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecParamDao extends JpaRepository<SpecParam,Long>, JpaSpecificationExecutor<SpecParam> {
}
