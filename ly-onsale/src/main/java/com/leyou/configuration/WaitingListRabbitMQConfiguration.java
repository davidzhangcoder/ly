package com.leyou.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WaitingListRabbitMQConfiguration {

    @Autowired
    public WaitingListConfiguration waitingListConfiguration;

    private static String DLTOPICEXCHANGE = "leyou.waitinglist.dl.topic.exchange";
    private static String DLQUEUE = "leyou.waitinglist.dl.queue";
    private static String DLROUTINGKEY = "leyou.waitinglist.dl.routing.key";


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
//        // 注册队列
//        Map<String,Object> params = new HashMap<>();
//        //声明当前队列绑定的死信交换机
//        params.put("x-dead-letter-exchange",dlTopicExchange);
//        //声明当前队列的死信路由键
//        params.put("x-dead-letter-routing-key",dlRoutingKey);

        return QueueBuilder.durable(waitingListConfiguration.getQueue())
                .withArgument("x-dead-letter-exchange", "leyou.waitinglist.dl.topic.exchange")        // 绑定死信队列交换机
                .withArgument("x-dead-letter-routing-key", "leyou.waitinglist.dl.routing.key")   // 绑定指定的routing-key
                .build();

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




    //死信交换机
    //创建交换机
    @Bean
    public DirectExchange dlDirectExchangeExchange(){
        return new DirectExchange(DLTOPICEXCHANGE,true,false);
    }

    //死信队列
    //创建队列
    @Bean
    public Queue dlQueue(){
        return new Queue(DLQUEUE,true);
    }

    //死信队列与死信交换机进行绑定
    //队列与交换机进行绑定
    @Bean
    public Binding BindingErrorQueueAndExchange(Queue dlQueue, DirectExchange dlDirectExchangeExchange){
        return BindingBuilder.bind(dlQueue).to(dlDirectExchangeExchange).with(DLROUTINGKEY);
    }

    //RepublishMessageRecover, 放入死信队列时（业务队列要先配置死信队列），原有队列不删除 : 使用配置的retry数，retry后还是有异常，放入死信队列时，但原有队列不删除
    //设置MessageRecoverer
//    @Bean
//    public MessageRecoverer messageRecoverer( @Qualifier("leyouWaitingListRabbitTemplate") RabbitTemplate rabbitTemplate ){
//        //AmqpTemplate和RabbitTemplate都可以
//        return new RepublishMessageRecoverer(rabbitTemplate,DLTOPICEXCHANGE,DLROUTINGKEY);
//    }

}
