package com.leyou.configuration;

import com.leyou.configuration.permission.MyFilterSecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class ResouceServerConfig extends ResourceServerConfigurerAdapter {

    public static final String RESOURCE_ID = "res1";

    @Autowired
    TokenStore tokenStore;

    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID)
                .tokenStore( tokenStore )
//                .tokenServices(tokenService()) //验证令牌的服务
                .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/user/query").permitAll()
                .antMatchers("/user/queryForOAuth2").permitAll()
                .antMatchers("/user/registerForOAuth2").permitAll()
                .antMatchers("/user/findUserByUsernameForOAuth2").permitAll()
                .antMatchers("/**").access("#oauth2.hasScope('ROLE_API')")
                .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);

        //.antMatchers("/**").access("#oauth2.hasScope('ROLE_API')")
    }

    //使用认证服务器，远程认证
//    //资源服务令牌解析服务
//    @Bean
//    public ResourceServerTokenServices tokenService() {
//        //使用远程服务请求授权服务器校验token,必须指定校验token 的url、client_id，client_secret
//        RemoteTokenServices service=new RemoteTokenServices();
//        service.setCheckTokenEndpointUrl("http://localhost:53020/oauth/check_token");
//        service.setClientId("c1");
//        service.setClientSecret("secret");
//        return service;
//    }

}
