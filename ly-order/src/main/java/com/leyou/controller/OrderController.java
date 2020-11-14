package com.leyou.controller;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.common.annotation.AccessLimit;
import com.leyou.common.dto.OnSaleStatus;
import com.leyou.dto.OrderDto;
import com.leyou.interceptor.UserInterceptor;
import com.leyou.pojo.Order;
import com.leyou.service.OrderService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RequestMapping("/order")
@RestController
@Api(tags = "订单相关接口", description = "提供订单相关的 Rest API")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private static AtomicInteger successCreated = new AtomicInteger(0);

    private static AtomicInteger failCreated = new AtomicInteger(0);


    //1.在OrderServiceImpl中redis修改库存要移到GoodsServiceImpl中
    /*
        1.
            zuul: 10s
            ribbon: 10s
            hystrix: 在OrderServiceImpl中更新库存后，模拟超时，检查库存和订单是否会回退
                     fall back方法是否调用

        2.
            zuul: 10s
            ribbon: 模拟超时，检查库存和订单是否会回退
                    在feign上的fall back方法是否调用
            hystrix: 不超时

        3.
            zuul: 模拟超时
                  fall back方法是否调用
                  答：应该Zuul上的fall back会调用
            ribbon: 不超时
            hystrix: 不超时

    */

    @PostMapping(value="/createOnSaleOrder")
    @ApiOperation(value = "创建订单接口，返回订单编号",notes = "创建订单")
    @ApiParam(name = "OnSaleStatus",required = true,value = "订单的json对象，包含订单条目和物流信息")
    @ApiResponse(code = 200, message = "订单创建成功")
    public void createOnSaleOrder(OnSaleStatus onSaleStatus) {
        UserInfo user = UserInterceptor.getUserInfo();
        orderService.createOnSaleOrder(onSaleStatus, user);
    }

    @PostMapping(value="/createOrder")
    @ApiOperation(value = "创建订单接口，返回订单编号",notes = "创建订单")
        //如使用ApiImplicitParam，那么ApiModel和ApiModelProperty信息不会在swagger-ui上显示
        //@ApiImplicitParam(name = "orderDto",required = true,value = "订单的json对象，包含订单条目和物流信息")
    @ApiParam(name = "orderDto",required = true,value = "订单的json对象，包含订单条目和物流信息")
    @ApiResponse(code = 200, message = "订单创建成功")
    public ResponseEntity<Long> createOrder(@RequestBody OrderDto orderDto){
        //创建订单
        UserInfo user = UserInterceptor.getUserInfo();
        Long orderId = null;
        try {
            orderId = orderService.createOrder(orderDto,user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(-1l);
        }

        return ResponseEntity.ok(orderId);
    }

    @GetMapping(value="testMethod")
    @AccessLimit( seconds = 17 , maxCount = 3 )
    public void testMethod(HttpServletRequest request) throws InterruptedException {
        System.out.println("testMethod");

        System.out.println(successCreated.get() + " failCreated: " + failCreated.get());
        successCreated.set(0);
        failCreated.set(0);

        TimeUnit.SECONDS.sleep(1);

        HttpSession session = request.getSession();
        session.setAttribute("test", "testRedisHttpSession");
        System.out.println(session.getAttribute("test"));
        //orderService.testMethod();
    }

    @GetMapping(value="orderTestFallBack/{id}")
    public void orderTestFallBack(@PathVariable(value="id") long id){
        System.out.println("orderTestFallBack");

        //orderService.orderTestFallBack(id);
    }

    @GetMapping(value="testLua")
    public void testLua() {
        orderService.testLua();
    }

}
