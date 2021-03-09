package com.leyou.auth.oauth2.client;

import com.leyou.domain.User;

public class UserClientFallBack implements UserClient{
    @Override
    public User query(String username, String password) {
        return null;
    }

    @Override
    public String seataUpdateAccount2(long amount) {
        return null;
    }

    @Override
    public String testUser() {
        return null;
    }
}
