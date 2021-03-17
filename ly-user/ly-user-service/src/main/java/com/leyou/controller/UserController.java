package com.leyou.controller;

import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.domain.Permission;
import com.leyou.domain.User;
import com.leyou.seata.TestSeataService;
import com.leyou.service.UserService;
import com.leyou.service.impl.UserServiceImpl;
import com.leyou.utils.TokenDecodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TestSeataService testSeataService;

    @Autowired
    private TokenDecodeUtil tokenDecodeUtil;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping( value = "check/{data}/{type}" )
    public ResponseEntity<Boolean> check(@PathVariable("data") String data,
                                         @PathVariable("type") Long type ) {
        return ResponseEntity.ok( userService.check( data , type ) );
    }

    @PostMapping( value = "code" )
    public ResponseEntity<Void> code( @RequestParam String phone ) {
        userService.code( phone );
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping( value = "register" )
    public ResponseEntity<Void> register( User user , @RequestParam String code ) {
        userService.register( user , code );
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping( value = "queryForOAuth2" )
    public ResponseEntity<User> queryForOAuth2(@RequestParam String username , @RequestParam String password ) {
        User user = userService.queryForOAuth2(username, password);
        if (user == null) {
            throw new LyException(ExceptionEnum.USER_PASSWORD_NOT_MATCH);
        }
        return ResponseEntity.ok( user );
    }

    @GetMapping( value = "query" )
    public ResponseEntity<User> query(@RequestParam String username , @RequestParam String password ) {
        User user = userService.query(username, password);
        if (user == null) {
            throw new LyException(ExceptionEnum.USER_PASSWORD_NOT_MATCH);
        }
        return ResponseEntity.ok( user );
    }

    @GetMapping( value = "findUserByUsernameForOAuth2" )
    public ResponseEntity<User> findUserByUsernameForOAuth2(@RequestParam String username ) {
        User user = userService.findUserByUsernameForOAuth2(username);
        return ResponseEntity.ok( user );
    }

    @PostMapping( value = "registerForOAuth2" )
    public ResponseEntity<Void> registerForOAuth2( User user ) {
        userService.registerForOAuth2( user );
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping( value = "getUserPermissions" )
    public List<Permission> getUserPermissions() {
        long userId = 0;
        try {
            userId = tokenDecodeUtil.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return Collections.emptyList();
        }
        return userService.getUserPermissions(userId);
    }





    @GetMapping( value = "seataUpdateAccount2" )
    public String seataUpdateAccount2(@RequestParam long amount){
        testSeataService.updateAccount2(amount);
        return "from amount: " + amount;
    }

    @GetMapping( value = "testUser" )
    @PreAuthorize("hasAnyAuthority('testUserPermission')")
    public String testUser(){
        return "testUser";
    }
}
