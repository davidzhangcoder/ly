package com.leyou.service.impl;

import com.leyou.common.dto.CartDto;
import com.leyou.common.dto.OnSaleStatus;
import com.leyou.common.utils.RedisKeyConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OnSaleServiceImplTest {

    @Autowired
    private RejectedExecutionHandler rejectedExecutionHandler;

    @Autowired
    private RedisTemplate redisTemplate;

    Logger logger = LoggerFactory.getLogger(OnSaleServiceImplTest.class);


    @Test
    public void testRedisOnSaleStatusList() {
        redisTemplate.getHashKeySerializer();
        redisTemplate.getHashValueSerializer();

        CartDto cartDto1 = new CartDto();
        cartDto1.setSkuId(11111l);
        CartDto cartDto2 = new CartDto();
        cartDto2.setSkuId(22222l);

        OnSaleStatus onSaleStatus1 = new OnSaleStatus( 1, Calendar.getInstance(), 1, 1l, "",1);
        onSaleStatus1.getCartDtoList().add(cartDto1);
        onSaleStatus1.getCartDtoList().add(cartDto2);
        OnSaleStatus onSaleStatus2 = new OnSaleStatus( 2, Calendar.getInstance(), 2, 2l, "",2);
        List<OnSaleStatus> list = new ArrayList<OnSaleStatus>();
        list.add(onSaleStatus1);
        list.add(onSaleStatus2);

        String key = RedisKeyConstants.HASH_ONSALESTATUS_BY_PRODUCT_ON_USERID;
        redisTemplate.boundHashOps(key).put(String.valueOf(1), list);

        List<OnSaleStatus> onSaleStatusListFromCache = (List<OnSaleStatus>)redisTemplate.boundHashOps(key).get(String.valueOf(1));
        System.out.println(onSaleStatusListFromCache);
    }


    @Test
    public void testRedisOnSaleStatus() {
        redisTemplate.getHashKeySerializer();
        redisTemplate.getHashValueSerializer();

        OnSaleStatus onSaleStatus = new OnSaleStatus( 1, Calendar.getInstance(), 1, 1l, "",1);
        onSaleStatus.setReason("存在未支付秒杀订单");
        String key = RedisKeyConstants.HASH_ONSALESTATUS_BY_PRODUCT_ON_USERID;
        redisTemplate.boundHashOps(key).put(String.valueOf(1), onSaleStatus);

        OnSaleStatus onSaleStatusFromCache = (OnSaleStatus)redisTemplate.boundHashOps(key).get(String.valueOf(1));
        System.out.println(onSaleStatusFromCache);
    }

    @Test
    public void testgrowPolicyRejectedExecutionHandler(){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5,
                5,
                0L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(10),
                Executors.defaultThreadFactory(),
                rejectedExecutionHandler
        );

        for (int i = 0; i < 30; i++) {
            Thread thread = new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "sleeping");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "awake");
            }, String.valueOf(i));
            threadPoolExecutor.submit(thread);

            logger.warn("activecount={}, maximumPoolSize={}, lineing={}, remaingcapacity={}" ,
                    threadPoolExecutor.getActiveCount(), threadPoolExecutor.getMaximumPoolSize(),
                    threadPoolExecutor.getQueue().size(), threadPoolExecutor.getQueue().remainingCapacity());

        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}