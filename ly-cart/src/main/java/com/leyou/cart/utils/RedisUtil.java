package com.leyou.cart.utils;

import com.leyou.common.utils.RedisKeyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class RedisUtil {

    private volatile static RedisUtil redisUtil;

    @Resource
    private RedisTemplate redisTemplate;

//    private RedisUtil() {
//
//    }
//
//    public static RedisUtil getCurrent() {
//
//        if (redisUtil == null) {
//            synchronized (RedisUtil.class) {
//                if(redisUtil == null)
//                    redisUtil = new RedisUtil();
//            }
//        }
//
//        return redisUtil;
//    }

    public long getUniqueID() {
        return redisTemplate.opsForValue().increment(RedisKeyConstants.UNIQUE_ID);
    }

}
