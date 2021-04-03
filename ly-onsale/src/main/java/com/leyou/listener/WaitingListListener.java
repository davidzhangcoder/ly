package com.leyou.listener;

import com.leyou.common.dto.OnSaleStatus;
import com.leyou.service.impl.OnSaleAsyncCreaterByUsingRedisAndRabbitMQ;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Component
public class WaitingListListener {

    @Autowired
    @Qualifier("onSaleThreadPool_OnSaleService")
    public Executor onSaleThreadPool;

    @Autowired
    private OnSaleAsyncCreaterByUsingRedisAndRabbitMQ onSaleAsyncCreaterByUsingRedis;

    @RabbitListener(queues = "leyou.waitinglist.queue")
    @RabbitHandler
    private void listenTransactionMessage(OnSaleStatus onSaleStatus, Message msg){

//        https://blog.csdn.net/g3230863/article/details/84777509
//        RabbitMQ 默认使用的是自动的确认模式，在投递成功之前，如果消费者的 TCP 连接 或者 channel 关闭了，这条消息就会丢失

//        https://segmentfault.com/a/1190000023736395
//        打开手动消息确认之后，只要我们这条消息没有成功消费，无论中间是出现消费者宕机还是代码异常，只要连接断开之后这条信息还没有被消费那么这条消息就会被重新放入队列再次被消费

//        int i = 1/0;

//        String hashTag = "_{OnSaleServiceImpl_snapUpOrder}";
//
//            Thread thread = new Thread(() -> {
//                    if (onSaleStatus != null) {
//
//                        onSaleAsyncCreaterByUsingRedis.snapUpOrder(onSaleStatus);
//                    }
//            });
//
//            onSaleThreadPool.execute( thread );
    }

}
