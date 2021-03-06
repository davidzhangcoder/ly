package com.leyou.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

//        pre: 请求被路由到源服务器之前的过滤器
//        routing: 处理将请求发送到源服务器之前的过滤器
//        post：响应从源服务器返回时执行的过滤器
//        error: 上述阶段中出现错误时执行的过滤器

@Component
public class ErrorFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("error detected");
        return null;
    }
}
