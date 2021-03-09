package com.leyou.auth.oauth2.service;

import com.leyou.auth.oauth2.utils.AuthToken;

public interface OAuth2Service {

    public AuthToken authorize(String username, String password, String clientId , String clientSecret, String grantType);

}
