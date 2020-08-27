package com.leyou.filter;

import com.leyou.auth.utils.JwtUtils;
import com.leyou.config.FilterProperties;
import com.leyou.config.JwtConfiguration;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
public class AuthFilter extends ZuulFilter {

    @Autowired
    private JwtConfiguration jwtConfiguration;

    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();

        HttpServletRequest request = ctx.getRequest();

        String requestURI = request.getRequestURI();

        return !isAllowPath(requestURI);
    }

    private boolean isAllowPath(String requestURI) {
        for (String allowPath : filterProperties.getAllowPaths()) {
            if( requestURI.contains(allowPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();

        HttpServletRequest request = ctx.getRequest();

        Cookie cookie = Arrays.stream(request.getCookies())
                .filter(x -> x.getName().equalsIgnoreCase(jwtConfiguration.getCookieName()))
                .findFirst().orElse(null);

        boolean stop = false;
        if(cookie==null) {
            stop = true;
        }

        try {
            JwtUtils.getInfoFromToken(cookie.getValue(), jwtConfiguration.getPublicKey());
        } catch (Exception e) {
            stop = true;
        }

        //TODO: 权限管理

        if(stop) {
            //token not authorized
            //未登录，拦截
            ctx.setSendZuulResponse(false);
            //返回状态码
            ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        }

        return null;
    }
}
