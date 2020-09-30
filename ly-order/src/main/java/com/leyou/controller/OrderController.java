package com.leyou.controller;

import com.leyou.dto.OrderDto;
import com.leyou.pojo.Order;
import com.leyou.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value="/createOrder")
    public ResponseEntity<Long> createOrder(@RequestBody OrderDto orderDto){
        //创建订单
        Long orderId = orderService.createOrder(orderDto);
        return ResponseEntity.ok(orderId);
    }

    @GetMapping(value="testMethod")
    public void testMethod() {
        System.out.println("testMethod");

        orderService.testMethod();
    }

    @GetMapping(value="orderTestFallBack/{id}")
    public void orderTestFallBack(@PathVariable(value="id") long id){
        System.out.println("orderTestFallBack");

        orderService.orderTestFallBack(id);
    }

}
