package com.leyou.auth.oauth2.controller;

import com.leyou.auth.oauth2.client.UserClient;
import com.leyou.auth.oauth2.config.AuthConfiguration;
import com.leyou.auth.oauth2.config.JwtConfiguration;
import com.leyou.auth.oauth2.service.OAuth2Service;
//import com.leyou.common.utils.CookieUtils;
import com.leyou.auth.oauth2.utils.AuthToken;
import com.leyou.common.utils.CookieUtils;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controler {

    @Autowired
    private JwtConfiguration jwtConfiguration;

    @Autowired
    private AuthConfiguration authConfiguration;

    @Autowired
    private OAuth2Service oAuth2Service;

    @Autowired
    private UserClient userClient;

    @GetMapping(value = "login" )
    public ResponseEntity<AuthToken> login(@RequestParam String username ,
                                      @RequestParam String password ,
                                      HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse) {

        AuthToken token = oAuth2Service.authorize(username, password, authConfiguration.getClientId(), authConfiguration.getClientSecret(), authConfiguration.getGrantType());

        String cookieName = jwtConfiguration.getCookieName();

        //put token into cookie
        //CookieUtils.setCookie( httpServletRequest , httpServletResponse , cookieName , token.getAccessToken() , "" );

        return ResponseEntity.ok(token);//.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "testUserFromOauth2" )
    public String testUserFromOauth2(){
        return userClient.testUser();
    }

}
