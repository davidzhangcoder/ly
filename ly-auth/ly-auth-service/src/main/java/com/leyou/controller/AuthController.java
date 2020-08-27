package com.leyou.controller;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.client.UserClient;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CookieUtils;
import com.leyou.config.JwtConfiguration;
import com.leyou.domain.User;
import com.leyou.service.AuthService;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserClient userClient;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtConfiguration jwtConfiguration;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping( value = "login" )
    public ResponseEntity<Void> login(@RequestParam String username ,
                                @RequestParam String password ,
                                HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse) {

        String token = authService.authorize(username, password);
        String cookieName = jwtConfiguration.getCookieName();

        //put token into cookie
        CookieUtils.setCookie( httpServletRequest , httpServletResponse , cookieName , token , "" );

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping( value = "verify" )
    public ResponseEntity<UserInfo> verify( @CookieValue( value = "LY_TOKEN") String token,
                        HttpServletRequest httpServletRequest,
                        HttpServletResponse httpServletResponse) {
        UserInfo userInfo = authService.verify(token);
        if(userInfo==null) {
            throw new LyException(ExceptionEnum.USER_UNAUTHORIZED);
        }

        try {
            String newToken = authService.getToken(userInfo);
            //put token into cookie
            String cookieName = jwtConfiguration.getCookieName();
            CookieUtils.setCookie( httpServletRequest , httpServletResponse , cookieName , newToken , "" );
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.AUTH_TOKEN_ERROR);
        }

        return ResponseEntity.ok(userInfo);
    }

}
