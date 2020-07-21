package com.leyou.dao;

import com.leyou.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandDao extends JpaRepository<Brand,Long>, JpaSpecificationExecutor<Brand> {
}
