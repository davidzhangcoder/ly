package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class LyHystrixDashBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(LyHystrixDashBoardApplication.class,args);
    }

}
