package com.leyou.service.impl;

import com.leyou.dao.UserDao;
import com.leyou.domain.User;
import com.leyou.service.UserService;
import com.leyou.utils.CodecUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Test
    public void testDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020,7,31, 23,59);
        System.out.println( calendar.getTime() );
        calendar.add( Calendar.MINUTE , 2 );
        System.out.println( calendar.getTime() );
    }

    @Test
    public void code() {

        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i = 1; i <= 1000000; i++) {
            int finalI = i;
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    userService.code( Thread.currentThread().getName()+":"+finalI);
                }
            });
        }

        System.out.println("Done");

    }

    /**
     * 注册5000个用户
     */
    @Test
    public void addUser(){
        User user = new User();
        for (int i = 1; i <= 1; i ++){
            user.setId(null);
            user.setCreated(new Date());
            user.setPhone("1883482"+String.format("%04d",i));
            user.setUsername("username"+i);
            user.setPassword("abcdefg"+i);

            String salt = CodecUtils.generateSalt();
            String encodePassword = CodecUtils.md5Hex(user.getPassword(), salt);

            user.setPassword(encodePassword);
            user.setSalt(salt);

            userDao.save(user);
        }
    }

}