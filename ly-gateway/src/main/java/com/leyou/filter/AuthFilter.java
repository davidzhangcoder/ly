package com.leyou.filter;


import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.EncryptUtil;
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
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class AuthFilter extends ZuulFilter {

    @Autowired
    private JwtConfiguration jwtConfiguration;

    @Autowired
    private FilterProperties filterProperties;

    //令牌头名字
    private static final String AUTHORIZE_TOKEN = "Authorization";

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

        String jwt = request.getHeader(AUTHORIZE_TOKEN);

        if(StringUtils.isEmpty(jwt) && request.getCookies()!=null) {
            Cookie cookie = Arrays.stream(request.getCookies())
                    .filter(x -> x.getName().equalsIgnoreCase(jwtConfiguration.getCookieName()))
                    .findFirst().orElse(null);

            jwt = cookie.getValue();
        }

        boolean stop = false;
        if( !StringUtils.isEmpty(jwt) ) {
            try {
                String jwtValue = jwt;
                if ( jwtValue.startsWith("bearer ") )
                    jwtValue = jwtValue.replaceFirst("bearer ","");

                if ( jwtValue.startsWith("Bearer ") )
                    jwtValue = jwtValue.replaceFirst("Bearer ","");

                JwtUtils.getInfoFromToken(jwtValue, jwtConfiguration.getPublicKey());
            } catch (Exception e) {
                stop = true;
            }
        }else
            stop = true;

        //TODO: 权限管理

        if(stop) {
            //token not authorized
            //未登录，拦截
            ctx.setSendZuulResponse(false);
            //返回状态码
            ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
            return null;
        }

        String token = jwt;

        if (!token.startsWith("bearer ") && !token.startsWith("Bearer ")) {
            //拼接token
            token = "bearer " + token;

        }

        ctx.addZuulRequestHeader("Authorization", token);


        //上面是leyou的代码 - 需要在cookie中传入令牌

//        //下面是引入OAuth2后,要把相应的权限传到微服务中 － 需要在header中传入 Authorization : Bearer 令牌的值
//
////        RequestContext ctx = RequestContext.getCurrentContext();
//        //从安全上下文中拿 到用户身份对象
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(!(authentication instanceof OAuth2Authentication)){
//
//            //token not authorized
//            //未登录，拦截
//            ctx.setSendZuulResponse(false);
//            //返回状态码
//            ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
//
//            return null;
//        }
//        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
//        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
//        //取出用户身份信息
//        String principal = userAuthentication.getName();
//
//        //取出用户权限
//        List<String> authorities = new ArrayList<>();
//        //从userAuthentication取出权限，放在authorities
//        userAuthentication.getAuthorities().stream().forEach(c->authorities.add(((GrantedAuthority) c).getAuthority()));
//
//        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
//        Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
//        Map<String,Object> jsonToken = new HashMap<>(requestParameters);
//        if(userAuthentication!=null){
//            jsonToken.put("principal",principal);
//            jsonToken.put("authorities",authorities);
//        }
//
//        //把身份信息和权限信息放在json中，加入http的header中,转发给微服务
//        ctx.addZuulRequestHeader("json-token", EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(jsonToken)));


        return null;
    }
}
