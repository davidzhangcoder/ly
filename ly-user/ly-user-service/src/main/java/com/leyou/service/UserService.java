package com.leyou.service;

import com.leyou.domain.User;

public interface UserService {
    public Boolean check(String data, Long type);

    public void code(String code);

    public void register(User user, String code);

    public User query(String username, String password);
}