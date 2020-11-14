package com.leyou.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    public static final String QUEUE_NOT_PAID = "queue.not.paid";

    /** 死信交换机 */
    public static final String DLX_EXCHANGE = "dlx.exchange";

    /** 延迟缓冲（按消息） */
    public static final String QUEUE_PAYMENT_DELAY = "queue.payment.delay";

    /**
     * not paid 队列
     * @return
     */
    @Bean
    public Queue notPaidQueue() {
        return new Queue(QUEUE_NOT_PAID, true);
    }

    /**
     * 延迟队列
     * @return
     */
    @Bean
    public Queue delayMessageQueue() {
        return QueueBuilder.durable(QUEUE_PAYMENT_DELAY)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)        // 消息超时进入死信队列，绑定死信队列交换机
                .withArgument("x-dead-letter-routing-key", QUEUE_NOT_PAID)   // 绑定指定的routing-key
                .build();
    }

    /***
     * 创建交换机
     * @return
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(DLX_EXCHANGE);
    }


    /***
     * 交换机与队列绑定
     * @param notPaidQueue
     * @param directExchange
     * @return
     */
    @Bean
    public Binding basicBinding(Queue notPaidQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(notPaidQueue)
                .to(directExchange)
                .with(QUEUE_NOT_PAID);
    }

    @Bean( name = "leyouOnSaleRabbitTemplate" )
    public RabbitTemplate leyouOnSaleRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate leyouSMSRabbitTemplate = new RabbitTemplate(connectionFactory);
        return leyouSMSRabbitTemplate;
    }


}
