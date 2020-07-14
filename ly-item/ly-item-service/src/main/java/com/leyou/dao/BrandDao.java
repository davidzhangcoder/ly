package com.leyou.dao;

import com.leyou.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BrandDao extends JpaRepository<Brand,Integer>, JpaSpecificationExecutor<Brand> {
}
