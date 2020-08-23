package com.leyou.service.impl;

import com.leyou.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

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
}