package com.leyou.auth.oauth2.client;

import com.leyou.domain.User;

public class UserClientFallBack implements UserClient{
    @Override
    public User query(String username, String password) {
        return null;
    }

    @Override
    public User queryForOAuth2(String username, String password) {
        return null;
    }

    @Override
    public User findUserByUsernameForOAuth2(String username) {
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
