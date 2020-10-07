package com.leyou.controller;

import com.leyou.dto.OrderDto;
import com.leyou.pojo.Order;
import com.leyou.service.OrderService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/order")
@RestController
@Api(tags = "订单相关接口", description = "提供订单相关的 Rest API")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value="/createOrder")
    @ApiOperation(value = "创建订单接口，返回订单编号",notes = "创建订单")
    //如使用ApiImplicitParam，那么ApiModel和ApiModelProperty信息不会在swagger-ui上显示
    //@ApiImplicitParam(name = "orderDto",required = true,value = "订单的json对象，包含订单条目和物流信息")
    @ApiParam(name = "orderDto",required = true,value = "订单的json对象，包含订单条目和物流信息")
    @ApiResponse(code = 200, message = "订单创建成功")
    public ResponseEntity<Long> createOrder(@RequestBody OrderDto orderDto){
        long start = System.currentTimeMillis();
        //创建订单
        Long orderId = orderService.createOrder(orderDto);

        long end = System.currentTimeMillis();
        System.out.println("orderService.createOrder(orderDto): " + (end - start));
        return ResponseEntity.ok(end - start);

        //return ResponseEntity.ok(orderId);
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
