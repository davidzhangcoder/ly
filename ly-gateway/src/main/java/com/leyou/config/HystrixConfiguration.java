package com.leyou.config;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//correct way should use following in application.yml
//#暴露全部的监控信息
//        management:
//        endpoints:
//        web:
//        exposure:
//        include: "*"

//@Configuration
public class HystrixConfiguration {

//    @Bean(name = "hystrixRegistrationBean")
//    public ServletRegistrationBean servletRegistrationBean() {
//        ServletRegistrationBean registration = new ServletRegistrationBean(
//                new HystrixMetricsStreamServlet(), "/hystrix.stream");
//        registration.setName("hystrixServlet");
//        registration.setLoadOnStartup(1);
//        return registration;
//    }
//
//    @Bean(name = "hystrixForTurbineRegistrationBean")
//    public ServletRegistrationBean servletTurbineRegistrationBean() {
//        ServletRegistrationBean registration = new ServletRegistrationBean(
//                new HystrixMetricsStreamServlet(), "/actuator/hystrix.stream");
//        registration.setName("hystrixForTurbineServlet");
//        registration.setLoadOnStartup(1);
//        return registration;
//    }


}
