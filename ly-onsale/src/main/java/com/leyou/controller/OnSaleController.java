package com.leyou.controller;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.common.annotation.AccessLimit;
import com.leyou.service.OnSaleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@RequestMapping("/onsale")
@RestController
//@Api(tags = "秒杀相关接口", description = "提供秒杀相关的 Rest API")
public class OnSaleController {

    @Autowired
    private OnSaleService onSaleService;

    @GetMapping(value="snapUpOrder")
    @ApiOperation(value = "创建秒杀订单接口，返回订单编号",notes = "创建秒杀订单")
        //如使用ApiImplicitParam，那么ApiModel和ApiModelProperty信息不会在swagger-ui上显示
        //@ApiImplicitParam(name = "orderDto",required = true,value = "订单的json对象，包含订单条目和物流信息")
    //@ApiParam(name = "onSaleProductID",required = true,value = "秒杀商品ID")
    @ApiResponse(code = 200, message = "秒杀订单创建成功")
    public ResponseEntity<Long> snapUpOrder(@RequestParam long onSaleProductID,
        HttpServletRequest request){
        long userID = 1;

        HttpSession session = request.getSession();
        long uniqueID = onSaleService.snapUpOrder(onSaleProductID, userID);

        return ResponseEntity.ok(uniqueID);
    }

    @GetMapping(value="snapUpOrderByUsingRedis")
    public ResponseEntity<Long> snapUpOrderByUsingRedis(@RequestParam long onSaleProductID, @RequestParam long userID, HttpServletRequest request) {
        //long userID = 2;

        HttpSession session = request.getSession();
        long uniqueID = onSaleService.snapUpOrderByUsingRedis(onSaleProductID, userID);

        return ResponseEntity.ok(uniqueID);
    }

    @GetMapping(value="snapUpOrderByUsingRabbitmq")
    public ResponseEntity<Long> snapUpOrderByUsingRabbitmq(@RequestParam long onSaleProductID, @RequestParam long userID , HttpServletRequest request){
        //long userID = 3;

        HttpSession session = request.getSession();
        long uniqueID = onSaleService.snapUpOrderByUsingRabbitmq(onSaleProductID, userID);

        return ResponseEntity.ok(uniqueID);
    }

    @GetMapping(value="queryOnSaleStatus")
    public ResponseEntity<Integer> queryOnSaleStatus(@RequestParam long userID, @RequestParam long uniqueID){
        int status = onSaleService.queryOnSaleStatus(userID , uniqueID);
        return ResponseEntity.ok(status);
    }

    @GetMapping(value="testMethod")
    public String testMethod() throws InterruptedException {
        System.out.println("testMethod");
        return "testMethod";
    }


}
