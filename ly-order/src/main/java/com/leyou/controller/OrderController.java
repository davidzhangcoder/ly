package com.leyou.controller;

import com.leyou.dto.OrderDto;
import com.leyou.pojo.Order;
import com.leyou.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value="/order")
    public ResponseEntity<Long> createOrder(@RequestBody OrderDto orderDto){
        //创建订单
        Long orderId = orderService.createOrder(orderDto);
        return ResponseEntity.ok(orderId);
    }

}
