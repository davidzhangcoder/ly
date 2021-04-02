package com.leyou.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WaitingListRabbitMQConfiguration {

    @Autowired
    public WaitingListConfiguration waitingListConfiguration;

    @Bean( name = "leyouWaitingListRabbitTemplate" )
    public RabbitTemplate leyouTransactionRabbitTemplate(ConnectionFactory connectionFactory,
                                                         RabbitTemplate.ConfirmCallback confirmCallBack){
        RabbitTemplate leyouTransactionRabbitTemplate = new RabbitTemplate(connectionFactory);
        leyouTransactionRabbitTemplate.setConfirmCallback(confirmCallBack);
        return leyouTransactionRabbitTemplate;
    }

    @Bean
    DirectExchange leyouTransactionDirectExchange(){
        // 注册一个 Direct 类型的交换机 默认持久化、非自动删除
        return new DirectExchange(waitingListConfiguration.getExchange());
    }

    //第一种方法
    @Bean
    Queue leyouTransactionQueue(){
        // 注册队列
        return new Queue(waitingListConfiguration.getQueue());
    }

    @Bean
    Binding leyouTransactionQueueToExchangeBinging(Queue leyouTransactionQueue, DirectExchange directExchange) {
        // 将队列以 info-msg 为绑定键绑定到交换机
        return BindingBuilder.bind(leyouTransactionQueue).to(directExchange).with(waitingListConfiguration.getRoutingKey());
    }

}
