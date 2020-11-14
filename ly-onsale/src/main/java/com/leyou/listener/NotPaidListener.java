package com.leyou.listener;

import com.leyou.common.dto.OnSaleStatus;
import com.leyou.common.utils.RedisKeyConstants;
import com.leyou.configuration.RabbitMQConfiguration;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
//@RabbitListener(queues = RabbitMQConfiguration.QUEUE_NOT_PAID)
public class NotPaidListener {

    @Autowired
    private RedisTemplate redisTemplate;

    /***
     * 监听消息
     * @param msg
     */
//    @RabbitHandler
    public void msg(@Payload Message msg){
        Map<String,String> messageMap = (Map<String, String>) SerializationUtils.deserialize(msg.getBody());
        String onSaleProductID = messageMap.get("onSaleProductID");
        String userID = messageMap.get("userID");
        String sentTime = messageMap.get("sentTime");
        String hashTag = messageMap.get("hashTag");

        System.out.println("---------");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("当前时间:"+dateFormat.format(new Date()));
        System.out.println("发送时间:"+sentTime);
        System.out.println("收到信息:"+msg);

        String onSaleStatusKey = RedisKeyConstants.HASH_ONSALESTATUS_BY_PRODUCT_ON_USERID + onSaleProductID + hashTag;
        OnSaleStatus onSaleStatus = (OnSaleStatus)redisTemplate.boundHashOps(onSaleStatusKey).get(String.valueOf(userID));
        onSaleStatus.setStatus(3);
        redisTemplate.boundHashOps(onSaleStatusKey).put(String.valueOf(userID), onSaleStatus);

        //back stock
        String productStockKey = RedisKeyConstants.GOODS_STOCK + onSaleProductID + hashTag;
        redisTemplate.boundValueOps(productStockKey).increment();
    }

}
