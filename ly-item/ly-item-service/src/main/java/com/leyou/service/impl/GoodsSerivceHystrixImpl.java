package com.leyou.service.impl;

import com.leyou.service.GoodsService;
import com.leyou.service.GoodsServiceHystrix;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "GoodsSerivceHystrixImpl")
public class GoodsSerivceHystrixImpl implements GoodsServiceHystrix {

    @Autowired
    private GoodsService goodsService;

    //配置HystrixCommand在服务提供方
    //1.服务提供方配execution.isolation.thread.timeoutInMilliseconds
    //2.调用方配ribbon的超时时间（调用方不需要配hystrix）
    @Override
    @HystrixCommand(fallbackMethod = "testFallBackHandler",
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="7000")
            })
    public void testFallBack(long id) {
        goodsService.testFallBack(id);
    }

    public void testFallBackHandler(long id , Throwable  e) throws Throwable {
        System.out.println(e);
        System.out.println("GoodsSerivceHystrixImpl - testFallBackHandler");
//        throw e;
    }

}

//                    ,
//                    @HystrixProperty(name="execution.isolation.strategy",value= "SEMAPHORE")

