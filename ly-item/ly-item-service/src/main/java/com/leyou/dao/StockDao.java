package com.leyou.dao;

import com.leyou.domain.Sku;
import com.leyou.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDao extends JpaRepository<Stock,Long>, JpaSpecificationExecutor<Stock> {
}
