package com.leyou.service.impl;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.common.dto.CartDto;
import com.leyou.common.dto.OnSaleStatus;
import com.leyou.dto.OrderDto;
import com.leyou.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderAsyncCreater {

    @Autowired
    private OrderService orderService;

    @Async(value="orderThreadPool_OrderService")
    public void createOrderAsync(OnSaleStatus onSaleStatus, UserInfo user) {
        try {
            OrderDto orderDto = new OrderDto();
            orderDto.setPaymentType(1);
            orderDto.setAddressId(1L);
            CartDto cartDto = new CartDto();
            cartDto.setSkuId(onSaleStatus.getGoodsId());
            cartDto.setNum(1);
            orderDto.getCarts().add(cartDto);
            Long orderID = orderService.createOrder(orderDto, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

}
