package com.leyou.service;


import com.leyou.enums.ExchangeType;

// 发送消息的目的地
public interface Destination {

    ExchangeType exchangeType();

    String queueName();

    String exchangeName();

    String routingKey();
}