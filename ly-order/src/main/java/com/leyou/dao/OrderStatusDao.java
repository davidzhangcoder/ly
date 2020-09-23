package com.leyou.dao;

import com.leyou.pojo.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderStatusDao extends JpaRepository<OrderStatus,Long>, JpaSpecificationExecutor<OrderStatus> {
}
