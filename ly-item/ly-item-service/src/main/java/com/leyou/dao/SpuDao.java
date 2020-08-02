package com.leyou.dao;

import com.leyou.domain.Spu;
import com.leyou.domain.SpuDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpuDao extends JpaRepository<Spu,Long>, JpaSpecificationExecutor<Spu> {
}
