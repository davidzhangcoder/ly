package com.leyou.configuration;

import com.leyou.service.impl.ConfirmCallBack;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTransactionConfiguration {

    @Autowired
    public TransactionQueueConfiguration transactionQueueConfiguration;

    @Bean( name = "leyouTransactionRabbitTemplate" )
    public RabbitTemplate leyouTransactionRabbitTemplate(ConnectionFactory connectionFactory,
                                                         ConfirmCallBack confirmCallBack){
        RabbitTemplate leyouTransactionRabbitTemplate = new RabbitTemplate(connectionFactory);
        leyouTransactionRabbitTemplate.setConfirmCallback(confirmCallBack);
        return leyouTransactionRabbitTemplate;
    }

    @Bean
    DirectExchange leyouTransactionDirectExchange(){
        // 注册一个 Direct 类型的交换机 默认持久化、非自动删除
        return new DirectExchange(transactionQueueConfiguration.getExchange());
    }

    //第一种方法
    @Bean
    Queue leyouTransactionQueue(){
        // 注册队列
        return new Queue(transactionQueueConfiguration.getQueue());
    }

    @Bean
    Binding leyouTransactionQueueToExchangeBinging(Queue leyouTransactionQueue, DirectExchange directExchange) {
        // 将队列以 info-msg 为绑定键绑定到交换机
        return BindingBuilder.bind(leyouTransactionQueue).to(directExchange).with(transactionQueueConfiguration.getRoutingKey());
    }

    //第二种方法
//    @Bean(name = "leyouTransactionQueue1")
//    Queue leyouTransactionQueue(){
//        // 注册队列
//        return new Queue(transactionQueueConfiguration.getQueue());
//    }
//
//    @Bean
//    Binding leyouTransactionQueueToExchangeBinging(Queue leyouTransactionQueue1, DirectExchange directExchange) {
//        // 将队列以 info-msg 为绑定键绑定到交换机
//        return BindingBuilder.bind(leyouTransactionQueue1).to(directExchange).with(transactionQueueConfiguration.getRoutingKey());
//    }

    //第三种方法
//    @Bean(name="leyouTransactionQueueName")
//    Queue leyouTransactionQueue(){
//        // 注册队列
//        return new Queue(transactionQueueConfiguration.getQueue());
//    }
//
//    @Bean
//    Binding leyouTransactionQueueToExchangeBinging(@Qualifier("leyouTransactionQueueName") Queue leyouTransactionQueue1, DirectExchange directExchange) {
//        // 将队列以 info-msg 为绑定键绑定到交换机
//        return BindingBuilder.bind(leyouTransactionQueue1).to(directExchange).with(transactionQueueConfiguration.getRoutingKey());
//    }





    @Bean
    Queue warnQueue(){
        return new Queue("warnMsgQueue");
    }

    @Bean
    Binding warnToExchangeBinging(Queue warnQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(warnQueue).to(directExchange).with("warn-msg");
    }

}
