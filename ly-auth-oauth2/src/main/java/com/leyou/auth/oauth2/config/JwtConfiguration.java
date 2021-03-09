package com.leyou.auth.oauth2.config;

import com.leyou.auth.utils.RsaUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
@ConfigurationProperties( prefix = "leyou.jwt" )
public class JwtConfiguration {

    private String secret;

    private String pubKeyPath;

    private String priKeyPath;

    private int expire;

    private String cookieName;


    private PublicKey publicKey;

    private PrivateKey privateKey;

    @PostConstruct
    public void init() throws Exception {
        File pubKeyFile = new File(pubKeyPath);
        File priKeyFile = new File(priKeyPath);

        if (!pubKeyFile.exists() || !priKeyFile.exists()) {
            RsaUtils.generateKey( pubKeyPath , priKeyPath , secret);
        }

        setPublicKey( RsaUtils.getPublicKey( pubKeyPath ) );
        setPrivateKey( RsaUtils.getPrivateKey(priKeyPath) );
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getPriKeyPath() {
        return priKeyPath;
    }

    public void setPriKeyPath(String priKeyPath) {
        this.priKeyPath = priKeyPath;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}
