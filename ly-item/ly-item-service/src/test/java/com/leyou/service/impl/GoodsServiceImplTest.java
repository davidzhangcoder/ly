package com.leyou.service.impl;

import com.leyou.seata.TestSeataService;
import com.leyou.service.BusinessServiceInterface;
import com.leyou.domain.Spu;
import com.leyou.service.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class GoodsServiceImplTest {

//    @Autowired
//    private AmqpTemplate amqpTemplate;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private BusinessServiceInterface businessServiceInterface;

    @Autowired
    private TestSeataService testSeataService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void testRedisson(){
        redissonClient.getBucket("v_redisson_1").set("1");
    }

    @Test
    public void testRedisson1(){
        System.out.println(redissonClient.getBucket("v_redisson_1").get());
    }


    @Test
    public void testRedis() {
        ValueOperations<String, Object> stringOperation = redisTemplate.opsForValue();

        stringOperation.set("a5_new","1");
    }

    @Test
    public void testRedis1() {
        ValueOperations<String, Object> stringOperation = redisTemplate.opsForValue();

        System.out.println(stringOperation.get("a5_new"));
    }


//    @Test
//    public void testSendMessage() {
//        Spu spu = goodsService.testPersistSpu(57, " Test1 ");
//
//        amqpTemplate.convertAndSend("item.update", spu.getId() );
//    }

    @Test
    public void testSeata(){
        testSeataService.updateAccount1();
    }

    @Test
    public void testSendMessage1() throws Exception {
//        Spu spu = goodsService.testPersistSpu(57, " Test1 ");

//        amqpTemplate.convertAndSend("item.update", spu.getId() );

        businessServiceInterface.saveOrder();
    }

    @Test
    public void testSendMessage2() throws Exception {
//        businessServiceInterface.updateAmount1();
        AtomicInteger count = new AtomicInteger(5);

        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i = 1; i <= 100; i++) {
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        //System.out.println( count.getAndIncrement() );
                        businessServiceInterface.updateAmount1(count.getAndIncrement());
                    } catch (Exception e) {
                        System.out.println("testSendMessage2 error: " + Thread.currentThread().getName() + e.getMessage() );
                    }
                }
            });
        }

        Thread.sleep(1000 * 300 );

    }


}