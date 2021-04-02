package com.leyou.configuration;

import com.leyou.common.enums.ExchangeType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties( prefix = "leyou.waitinglist" )
public class WaitingListConfiguration {

    private String exchange;

    private String queue;

    private String routingKey;

    private String exchangeType;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public ExchangeType getExchangeType() {
        if(!StringUtils.isEmpty(exchangeType)) {
            if (exchangeType.equalsIgnoreCase(ExchangeType.FANOUT.getType()))
                return ExchangeType.FANOUT;
            else if (exchangeType.equalsIgnoreCase(ExchangeType.DIRECT.getType()))
                return ExchangeType.DIRECT;
            else if (exchangeType.equalsIgnoreCase(ExchangeType.TOPIC.getType()))
                return ExchangeType.TOPIC;
        }
        return null;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

}
