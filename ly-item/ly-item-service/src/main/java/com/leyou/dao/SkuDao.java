package com.leyou.dao;

import com.leyou.domain.Category;
import com.leyou.domain.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuDao extends JpaRepository<Sku,Long>, JpaSpecificationExecutor<Sku> {

    public List<Sku> findSkuBySpuId(long spuid);

    @Query(value="SELECT a.*  FROM tb_sku a WHERE a.id in ( :skuIds )" ,nativeQuery=true)
    //b.stock can not set into Sku here
    //@Query(value="SELECT a.* , b.stock as stock FROM tb_sku a , tb_stock b WHERE a.id in ( :skuIds ) and a.id = b.sku_id" ,nativeQuery=true)
    public List<Sku> getSkusWithStockBySkuIDs(List<Long> skuIds);
}
