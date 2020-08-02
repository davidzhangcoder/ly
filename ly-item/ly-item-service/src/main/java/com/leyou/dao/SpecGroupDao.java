package com.leyou.dao;

import com.leyou.domain.Category;
import com.leyou.domain.SpecGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecGroupDao extends JpaRepository<SpecGroup,Long>, JpaSpecificationExecutor<SpecGroup> {

    public List<SpecGroup> findByCategory_Id(long cid);
}
