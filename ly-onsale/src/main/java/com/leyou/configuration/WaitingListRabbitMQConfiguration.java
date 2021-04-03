package com.leyou.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WaitingListRabbitMQConfiguration {

    @Autowired
    public WaitingListConfiguration waitingListConfiguration;

//    @Bean
//    public ConnectionFactory getConnectionFactory(){
//        return new CachingConnectionFactory();
//    }

//    @Bean
//    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        //使用jackson进行消息序列与反序列
//        factory.setMessageConverter(new Jackson2JsonMessageConverter());
//        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 开启手动 ack
//        return factory;
//    }

    @Bean( name = "leyouWaitingListRabbitTemplate" )
    public RabbitTemplate leyouTransactionRabbitTemplate(ConnectionFactory connectionFactory,
                                                         RabbitTemplate.ConfirmCallback confirmCallBack,
                                                         RabbitTemplate.ReturnCallback returnCallback,
                                                         MessageConverter messageConverter){
        //connectionFactory comes from RabbitAutoConfiguration
        RabbitTemplate leyouTransactionRabbitTemplate = new RabbitTemplate(connectionFactory);
        leyouTransactionRabbitTemplate.setConfirmCallback(confirmCallBack);
        leyouTransactionRabbitTemplate.setReturnCallback(returnCallback);
        leyouTransactionRabbitTemplate.setMessageConverter(messageConverter);

        //因为没有用RabbitAutoConfiguration中的public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory)，所以要
        leyouTransactionRabbitTemplate.setMandatory(true);

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
        return QueueBuilder.durable(waitingListConfiguration.getQueue()).build();
//        return new Queue(waitingListConfiguration.getQueue());
    }

    @Bean
    Binding leyouTransactionQueueToExchangeBinging(Queue leyouTransactionQueue, DirectExchange leyouTransactionDirectExchange) {
        // 将队列以 info-msg 为绑定键绑定到交换机
        return BindingBuilder.bind(leyouTransactionQueue).to(leyouTransactionDirectExchange).with(waitingListConfiguration.getRoutingKey());
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        return new Jackson2JsonMessageConverter();
    }

}
