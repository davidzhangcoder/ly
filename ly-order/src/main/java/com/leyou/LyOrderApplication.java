package com.leyou;

import com.alibaba.cloud.seata.feign.SeataFeignClientAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//解决以下错误，所以exclude SeataFeignClientAutoConfiguration
//The bean 'feignHystrixBuilder',
//        defined in class path resource
//[org/springframework/cloud/sleuth/instrument/web/client/feign/TraceFeignClientAutoConfiguration.class],
//        could not be registered. A bean with that name has already been defined in class path resource
//[com/alibaba/cloud/seata/feign/SeataFeignClientAutoConfiguration.class] and overriding is disabled.
@SpringBootApplication(exclude = {SeataFeignClientAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class LyOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyOrderApplication.class);
    }
}
