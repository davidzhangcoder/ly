package com.leyou.service.impl;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import com.leyou.client.UserClient;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.config.JwtConfiguration;
import com.leyou.domain.User;
import com.leyou.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PrivateKey;
import java.security.PublicKey;

@Service( value = "AuthServiceImpl" )
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtConfiguration jwtConfiguration;

    private Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public String authorize(String username, String password) {

        User user = userClient.query(username, password);

        if (user == null) {
            throw new LyException(ExceptionEnum.USER_PASSWORD_NOT_MATCH);
        }

        UserInfo userInfo = new UserInfo(user.getId(), username);
        try{
            String token = getToken(userInfo);
            return token;
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.AUTH_TOKEN_ERROR);
        }
    }

    @Override
    public String getToken(UserInfo userInfo) throws Exception {
        PrivateKey privateKey = jwtConfiguration.getPrivateKey();
        String token = JwtUtils.generateToken(userInfo, privateKey, jwtConfiguration.getExpire());
        logger.info("[授权服务] － 令牌已产生: {}", token);
        return token;
    }

    @Override
    public UserInfo verify(String token) {
        PublicKey publicKey = jwtConfiguration.getPublicKey();
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, publicKey);

            return userInfo;
        } catch (Exception e) {
            logger.info("[授权服务] － 令牌已破坏: {}" , token , e);
        }
        return null;
    }
}
