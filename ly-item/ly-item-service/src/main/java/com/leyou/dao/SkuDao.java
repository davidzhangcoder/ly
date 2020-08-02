package com.leyou.dao;

import com.leyou.domain.Category;
import com.leyou.domain.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuDao extends JpaRepository<Sku,Long>, JpaSpecificationExecutor<Sku> {

    public List<Sku> findSkuBySpuId(long spuid);
}
