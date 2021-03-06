package com.leyou.config;

import com.leyou.interceptor.AccessLimitInterceptor;
import com.leyou.interceptor.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    public UserInterceptor getUserInterceptor(){
        return new UserInterceptor();
    }

    @Bean
    public AccessLimitInterceptor getAccessLimitInterceptor(){
        return new AccessLimitInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getUserInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui.html")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/swagger-resources/**");
        registry.addInterceptor(getAccessLimitInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui.html")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/swagger-resources/**");
    }

}
