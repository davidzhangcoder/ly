package com.leyou.dao;

import com.leyou.domain.Sku;
import com.leyou.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDao extends JpaRepository<Stock,Long>, JpaSpecificationExecutor<Stock> {
    @Modifying
    @Query(value="update tb_stock set stock = stock - :num where sku_id = :skuId and stock >= :num" , nativeQuery=true)
    int decreaseStock(Long skuId, Integer num);

    public Stock getStockBySkuId(Long skuId);
}
