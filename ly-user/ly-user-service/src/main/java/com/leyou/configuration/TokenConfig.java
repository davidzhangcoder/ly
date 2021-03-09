package com.leyou.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.security.interfaces.RSAPublicKey;

/**
 * @author Administrator
 * @version 1.0
 **/
@Configuration
public class TokenConfig {

    @Autowired
    private ResourceJwtConfiguration resourceJwtConfiguration;


//    private String SIGNING_KEY = "uaa123";

    @Bean
    public TokenStore tokenStore() {
        //JWT令牌存储方案
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        RSAPublicKey publicKey = (RSAPublicKey) resourceJwtConfiguration.getPublicKey();
        converter.setVerifier(new RsaVerifier(publicKey));

//        converter.setSigningKey(SIGNING_KEY); //对称秘钥，资源服务器使用该秘钥来验证

        return converter;
    }

   /* @Bean
    public TokenStore tokenStore() {
        //使用内存存储令牌（普通令牌）
        return new InMemoryTokenStore();
    }*/
}
