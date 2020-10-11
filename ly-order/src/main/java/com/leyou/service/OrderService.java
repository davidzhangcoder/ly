package com.leyou.service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.dto.OrderDto;
import com.leyou.pojo.Order;

import java.util.List;

public interface OrderService {
    public Long createOrder(OrderDto orderDto, UserInfo user) throws Exception;

    public void testMethod();

    public void orderTestFallBack(long id);
}
