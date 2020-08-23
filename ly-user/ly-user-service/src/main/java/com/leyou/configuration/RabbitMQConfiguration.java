package com.leyou.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties( prefix = "leyou.sms" )
public class RabbitMQConfiguration {

    private String smsExchange;

    private String smsRouteringKey;

    public String getSmsExchange() {
        return smsExchange;
    }

    public void setSmsExchange(String smsExchange) {
        this.smsExchange = smsExchange;
    }

    public String getSmsRouteringKey() {
        return smsRouteringKey;
    }

    public void setSmsRouteringKey(String smsRouteringKey) {
        this.smsRouteringKey = smsRouteringKey;
    }
}
