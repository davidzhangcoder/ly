package com.leyou.interceptor;

import com.leyou.common.annotation.AccessLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public class AccessLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    @Qualifier(value="accessLimit")
    private DefaultRedisScript accessLimitLuaDefaultRedisScript;

    private Logger logger = LoggerFactory.getLogger(AccessLimitInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if( handler instanceof HandlerMethod ) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimitAnnotation = handlerMethod.getMethodAnnotation(AccessLimit.class);

            if( accessLimitAnnotation == null ) {
                //没有AccessLimit annotation, 放行
                return true;
            }

            int seconds = accessLimitAnnotation.seconds();
            int maxCount = accessLimitAnnotation.maxCount();
            boolean needLogin = accessLimitAnnotation.needLogin();

            String remoteAddr = request.getRemoteAddr();
            String localAddr = request.getLocalAddr();
            String requestURI = request.getRequestURI();
            logger.warn("remoteAddr = {}, localAddr = {}, requestURI = {}", remoteAddr, localAddr , requestURI);

            remoteAddr = remoteAddr.replaceAll("\\.","_");
            //spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本异常，此处拿到原redis的connection执行脚本
            // search_{192168110} , 10 10 10000
            List<String> keys = Arrays.asList(requestURI+"_{"+remoteAddr+"}");
            List<String> args = Arrays.asList(String.valueOf(seconds*1000), String.valueOf(maxCount), String.valueOf(System.currentTimeMillis()));
            Long result = (Long)redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    Object nativeConnection = connection.getNativeConnection();
                    // 集群模式和单点模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                    // 集群
                    if (nativeConnection instanceof JedisCluster) {
                        return (Long) ((JedisCluster) nativeConnection)
                                .eval(accessLimitLuaDefaultRedisScript.getScriptAsString(), keys, args);
                    }

                    // 单点
                    else if (nativeConnection instanceof Jedis) {
                        return (Long) ((Jedis) nativeConnection)
                                .eval(accessLimitLuaDefaultRedisScript.getScriptAsString(), keys, args);
                    }
                    return null;
                }
            });

            if (result != null) {
                if (result == 1) {
                    System.out.println("Access Limit - success");
                    return true;
                } else if (result == 0) {
                    System.out.println("Access Limit - over maxCount");
                    return false;
                }
            }
        }

        return true;
    }
}
