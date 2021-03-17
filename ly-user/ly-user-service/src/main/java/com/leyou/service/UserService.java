package com.leyou.service;

import com.leyou.domain.Permission;
import com.leyou.domain.User;

import java.util.List;

public interface UserService {
    public Boolean check(String data, Long type);

    public void code(String code);

    public void register(User user, String code);

    public User query(String username, String password);


    public User queryForOAuth2(String username, String password);

    public User findUserByUsernameForOAuth2(String username);

    public void registerForOAuth2(User user);

    public List<Permission> getUserPermissions(long userId);
}
