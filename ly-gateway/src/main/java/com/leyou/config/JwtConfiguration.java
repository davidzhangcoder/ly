package com.leyou.config;

import com.leyou.auth.utils.RsaUtils;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PublicKey;

@Configuration
@ConfigurationProperties( prefix = "leyou.jwt" )
public class JwtConfiguration {

    private String pubKeyPath;

    private String cookieName;

    private PublicKey publicKey;

    @PostConstruct
    public void init() throws Exception {
        File pubKeyFile = new File(pubKeyPath);


        if (!pubKeyFile.exists()) {
            throw new LyException(ExceptionEnum.AUTH_PUBLIC_KEY_NOT_EXIST);
        }

        setPublicKey( RsaUtils.getPublicKey( pubKeyPath ) );
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
