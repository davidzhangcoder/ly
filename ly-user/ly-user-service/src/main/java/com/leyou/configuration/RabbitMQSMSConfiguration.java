package com.leyou.configuration;

import com.leyou.service.impl.ConfirmCallBack;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties( prefix = "leyou.sms" )
public class RabbitMQSMSConfiguration {

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

    @Bean( name = "leyouSMSRabbitTemplate" )
    public RabbitTemplate leyouSMSRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate leyouSMSRabbitTemplate = new RabbitTemplate(connectionFactory);
        return leyouSMSRabbitTemplate;
    }

}
