package com.leyou.service.impl;

import com.leyou.configuration.TransactionQueueConfiguration;
import com.leyou.domain.TransactionalMessage;

import com.leyou.enums.ExchangeType;
import com.leyou.service.*;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;


import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RabbitTransactionalMessageService implements TransactionalMessageService {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private TransactionalMessageManagementService managementService;

    @Autowired
    private TransactionQueueConfiguration transactionQueueConfiguration;

//    public RabbitTransactionalMessageService(AmqpAdmin amqpAdmin, TransactionalMessageManagementService managementService) {
//        this.amqpAdmin = amqpAdmin;
//        this.managementService = managementService;
//    }

    private static final ConcurrentMap<String, Boolean> QUEUE_ALREADY_DECLARE = new ConcurrentHashMap<>();

    ExecutorService threadPool = Executors.newFixedThreadPool(5);

    @Override
    public void sendTransactionalMessage(String businessKey , String content) {
        DefaultTxMessage message = new DefaultTxMessage();
        message.setBusinessKey(businessKey);
        message.setBusinessModule("SAVE_ORDER");
        message.setContent(content);

        String queueName = transactionQueueConfiguration.getQueue();
        String exchangeName = transactionQueueConfiguration.getExchange();
        String routingKey = transactionQueueConfiguration.getRoutingKey();
        ExchangeType exchangeType = transactionQueueConfiguration.getExchangeType();

//        DefaultDestination destination = new DefaultDestination();
//        destination.setExchangeName("tm.test.exchange");
//        destination.setQueueName("tm.test.queue");
//        destination.setRoutingKey("tm.test.key");
//        destination.setExchangeType(ExchangeType.DIRECT);
//
//        String queueName = destination.queueName();
//        String exchangeName = destination.exchangeName();
//        String routingKey = destination.routingKey();
//        ExchangeType exchangeType = destination.exchangeType();

////        // 原子性的预声明
//        QUEUE_ALREADY_DECLARE.computeIfAbsent(queueName, k -> {
//            Queue queue = new Queue(queueName);
//            amqpAdmin.declareQueue(queue);
//            Exchange exchange = new CustomExchange(exchangeName, exchangeType.getType());
//            amqpAdmin.declareExchange(exchange);
//            Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
//            amqpAdmin.declareBinding(binding);
//            return true;
//        });

        TransactionalMessage record = new TransactionalMessage();
        record.setCreateTime(LocalDateTime.now());
        record.setEditTime(LocalDateTime.now());
        record.setCreator("admin");
        record.setEditor("admin");
        record.setDeleted(0);
        record.setQueueName(queueName);
        record.setExchangeName(exchangeName);
        record.setExchangeType(exchangeType.getType());
        record.setRoutingKey(routingKey);
        record.setBusinessModule(message.businessModule());
        record.setBusinessKey(message.businessKey());
        //String content = message.content();
        // 保存事务消息记录
        managementService.saveTransactionalMessageRecord(record, content);
        // 注册事务同步器
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {

                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        managementService.sendMessageSync(record, content);
                    }
                });

                //managementService.sendMessageSync(record, content);
            }
        });
    }
}