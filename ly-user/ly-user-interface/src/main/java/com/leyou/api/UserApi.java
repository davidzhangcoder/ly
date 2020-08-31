package com.leyou.api;

import com.leyou.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/user")
public interface UserApi {

    @GetMapping( value = "query" )
    public User query(@RequestParam String username , @RequestParam String password );

}