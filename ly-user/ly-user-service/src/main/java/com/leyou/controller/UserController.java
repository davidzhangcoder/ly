package com.leyou.controller;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.domain.User;
import com.leyou.seata.TestSeataService;
import com.leyou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TestSeataService testSeataService;

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

    @GetMapping( value = "query" )
    public ResponseEntity<User> query(@RequestParam String username , @RequestParam String password ) {
        User user = userService.query(username, password);
        if (user == null) {
            throw new LyException(ExceptionEnum.USER_PASSWORD_NOT_MATCH);
        }
        return ResponseEntity.ok( user );
    }

    @GetMapping( value = "seataUpdateAccount2" )
    public String seataUpdateAccount2(@RequestParam long amount){
        testSeataService.updateAccount2(amount);
        return "from amount: " + amount;
    }
}
