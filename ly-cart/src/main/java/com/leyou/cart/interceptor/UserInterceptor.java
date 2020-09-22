package com.leyou.cart.interceptor;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.cart.config.JwtConfiguration;
import com.leyou.common.utils.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtConfiguration jwtConfiguration;

    private static ThreadLocal<UserInfo> threadLocal = new ThreadLocal<UserInfo>();

    private Logger logger = LoggerFactory.getLogger(UserInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler)  {

        String token = CookieUtils.getCookieValue(request, jwtConfiguration.getCookieName());

        UserInfo userInfo = null;
        try {
            userInfo = JwtUtils.getInfoFromToken(token, jwtConfiguration.getPublicKey());

            threadLocal.set(userInfo);

            return true;
        } catch (Exception e) {
            logger.error("[购物车服务] － 解析用户身份失败",e);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        threadLocal.remove();
    }

    public static UserInfo getUserInfo(){
        return threadLocal.get();
    }
}
