package com.leyou.dao;

import com.leyou.pojo.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailDao extends JpaRepository<OrderDetail,Long>, JpaSpecificationExecutor<OrderDetail> {
}
