package com.leyou.listener;

import com.leyou.common.utils.JsonUtils;
import com.leyou.service.BusinessServiceInterface;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TransactionMessageListener {

    @Autowired
    private BusinessServiceInterface businessServiceInterface;

//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "leyou.transaction.queue",durable = "true"),
//            exchange = @Exchange(name = "leyou.transaction.exchange",type = ExchangeTypes.DIRECT),
//            key = {"leyou.transaction.routingKey"}
//    ))
    @RabbitListener(queues = "leyou.transaction.queue")
    private void listenTransactionMessage(String content) {
        Map<String, String> map = JsonUtils.parseMap(content, String.class, String.class);
        String toAccountId = map.get("toAccountId");
        String amount = map.get("amount");
        System.out.println("updateAmount2 : " + Thread.currentThread().getName());

        businessServiceInterface.updateAmount2(toAccountId , amount);
    }

}
