package com.leyou.controller;

import com.leyou.domain.User;
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

    @GetMapping( value = "check/{data}/{type}query" )
    public ResponseEntity<User> query(@RequestParam String username , @RequestParam String password ) {
        return ResponseEntity.ok( userService.query( username , password ) );
    }
}
