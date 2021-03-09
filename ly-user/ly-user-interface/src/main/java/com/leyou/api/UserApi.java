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

    @GetMapping( value = "queryForOAuth2" )
    public User queryForOAuth2(@RequestParam String username , @RequestParam String password );

    @GetMapping( value = "findUserByUsernameForOAuth2" )
    public User findUserByUsernameForOAuth2(@RequestParam String username );

    @GetMapping( value = "seataUpdateAccount2" )
    public String seataUpdateAccount2(@RequestParam long amount);

    @GetMapping( value = "testUser" )
    public String testUser();

}
