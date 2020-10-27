package com.leyou.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    public void testRedis() {
        ValueOperations<String, Object> stringOperation = redisTemplate.opsForValue();

        stringOperation.set("a2","2");
    }

}