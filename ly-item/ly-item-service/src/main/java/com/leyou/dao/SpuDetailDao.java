package com.leyou.dao;

import com.leyou.domain.SpuDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpuDetailDao extends JpaRepository<SpuDetail,Long>, JpaSpecificationExecutor<SpuDetail> {

    public SpuDetail findBySpuId( long spuid );

}
