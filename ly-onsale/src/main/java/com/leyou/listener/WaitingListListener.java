package com.leyou.listener;

import com.leyou.common.dto.OnSaleStatus;
import com.leyou.common.utils.RedisKeyConstants;
import com.leyou.service.impl.OnSaleAsyncCreaterByUsingRedisAndRabbitMQ;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Component
public class WaitingListListener {

    @Autowired
    @Qualifier("onSaleThreadPool_OnSaleService")
    public Executor onSaleThreadPool;

    @Autowired
    private OnSaleAsyncCreaterByUsingRedisAndRabbitMQ onSaleAsyncCreaterByUsingRedisAndRabbitMQ;

    @Autowired
    @Qualifier("redisTemplateLeyou")
    private RedisTemplate redisTemplate;

    @RabbitListener(queues = "leyou.waitinglist.queue")
    @RabbitHandler
    private void listenTransactionMessage(OnSaleStatus onSaleStatus, Message msg, Channel channel) throws IOException {

//        https://blog.csdn.net/g3230863/article/details/84777509
//        RabbitMQ 默认使用的是自动的确认模式，在投递成功之前，如果消费者的 TCP 连接 或者 channel 关闭了，这条消息就会丢失

//        https://segmentfault.com/a/1190000023736395
//        打开手动消息确认之后，只要我们这条消息没有成功消费，无论中间是出现消费者宕机还是代码异常，只要连接断开之后这条信息还没有被消费那么这条消息就会被重新放入队列再次被消费

//        https://blog.csdn.net/qq_38846242/article/details/84958640?spm=1001.2014.3001.5501
//        手动确认情况下，推荐在消息消费失败时，将消息放入死信队列（requeue=false）- 需要配死信队列


        try {
            String hashTag = "_{OnSaleServiceImpl_snapUpOrder}";

//            System.out.println("test retry, retry: " + getRetryCount(properties));
            //测试代码
//            if( onSaleStatus.getUniqueID() % 9 > 5 ) {
//                int i = 1/0;
//            }

            if (onSaleStatus != null) {
                //todo:
                //optimize here: not use @async here, where listener handle this, suppose it is in different thread
                onSaleAsyncCreaterByUsingRedisAndRabbitMQ.snapUpOrder(onSaleStatus);
            }

            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
            System.out.println("消费者: ACK , Channel Number: " + channel.getChannelNumber() +" , " + onSaleStatus.getUniqueID());

//            Tips: 代码示例中没有涉及数据库事务，若消费程序使用了声明式的事务@Transactional，在捕获异常后要手动回滚事务
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        catch(Exception e){
//            channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, false);
            System.out.println("消费者: Not ACK" +" , " + onSaleStatus.getUniqueID());

            String hashTag = "_{OnSaleServiceImpl_snapUpOrder}";
            String onSaleStatusKey = RedisKeyConstants.HASH_ONSALESTATUS + hashTag;
            long userID = onSaleStatus.getUserID();
            List<OnSaleStatus> onSaleStatusList = (List<OnSaleStatus>) redisTemplate.boundHashOps(onSaleStatusKey).get(String.valueOf(userID));
            if( onSaleStatusList != null ) {
                for (OnSaleStatus saleStatus : onSaleStatusList) {
                    if (saleStatus.getUniqueID() == onSaleStatus.getUniqueID()) {
                        System.out.println("消费者: Not ACK - 处理订单有错误 : " + e.getMessage());
                        onSaleStatus.setStatus(4);
                        onSaleStatus.setReason("消费者: Not ACK - 处理订单有错误");
                        List<OnSaleStatus> onSaleStatusListTemp = onSaleStatusList.stream().filter(a -> a.getUniqueID() != onSaleStatus.getUniqueID()).collect(Collectors.toList());
                        onSaleStatusListTemp.add(onSaleStatus);
                        redisTemplate.boundHashOps(onSaleStatusKey).put(String.valueOf(userID), onSaleStatusListTemp);
                    }
                }
            }

            //消费消息有错误时使用nack，并且requeue参数传false
            channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, false);

            //如配spring.listener.retry.enabled=true，抛出下面2个都可以
            //如配spring.listener.retry.enabled=false，需要抛出AmqpRejectAndDontRequeueException，即刻reject该消息，有死信队列则会被放置其中（还没测试过）
            //throw new AmqpRejectAndDontRequeueException("消费者: Not ACK - AmqpRejectAndDontRequeueException");
            //throw e;
        }
    }

    /**
     * 获取消息失败次数
     * @param properties
     * @return
     */
    public long getRetryCount(AMQP.BasicProperties properties){
        long retryCount = 0L;
        Map<String,Object> header = properties.getHeaders();
        if(header != null && header.containsKey("x-death")){
            List<Map<String,Object>> deaths = (List<Map<String,Object>>)header.get("x-death");
            if(deaths.size()>0){
                Map<String,Object> death = deaths.get(0);
                retryCount = (Long)death.get("count");
            }
        }
        return retryCount;
    }

}


