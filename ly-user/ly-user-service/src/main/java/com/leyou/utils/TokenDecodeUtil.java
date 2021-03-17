package com.leyou.utils;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.configuration.ResourceJwtConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenDecodeUtil {

    @Autowired
    private ResourceJwtConfiguration jwtConfiguration;

    public long getUserId() throws Exception {
        long userId = 0;

        OAuth2AuthenticationDetails authentication = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String jwtValue = authentication.getTokenValue();

        UserInfo infoFromToken = JwtUtils.getInfoFromToken(jwtValue, jwtConfiguration.getPublicKey());

        return infoFromToken.getId();
    }
}
