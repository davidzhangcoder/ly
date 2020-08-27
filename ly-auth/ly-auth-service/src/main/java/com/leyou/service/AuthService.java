package com.leyou.service;

import com.leyou.auth.pojo.UserInfo;

public interface AuthService {
    public String authorize(String username, String password);

    public UserInfo verify(String token);

    public String getToken(UserInfo userInfo) throws Exception;
}
