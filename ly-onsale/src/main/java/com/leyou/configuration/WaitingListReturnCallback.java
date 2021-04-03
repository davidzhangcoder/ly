package com.leyou.configuration;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class WaitingListReturnCallback implements RabbitTemplate.ReturnCallback {

    //要触发 ReturnCallback,必须要有以下配置
//    rabbitmq:
//        publisher-returns: true
//            template:
//                mandatory: true

//    https://blog.csdn.net/qq_38846242/article/details/84256383
//    实现ReturnCallback并重写returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey)回调方法，可以确认消息从EXchange路由到Queue失败。注意：这里的回调是一个失败回调，只有消息从Exchange路由到Queue失败才会回调这个方法

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("WaitingListReturnCallback");
        System.out.println("message: " + message.toString());
        System.out.println("replyCode: " + replyCode);
        System.out.println("replyText: " + replyText);
        System.out.println("exchange: " + exchange);
        System.out.println("routingKey: " + routingKey);
    }
}
