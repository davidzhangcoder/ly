package com.leyou.service.impl;

import com.leyou.common.dto.OnSaleStatus;
import com.leyou.common.cache.OrderDtoCache;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.RedisKeyConstants;
import com.leyou.configuration.RabbitMQConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class OnSaleAsyncCreater {

    private Logger logger = LoggerFactory.getLogger(OnSaleAsyncCreater.class);

    @Autowired
    @Qualifier(value="onsale")
    private DefaultRedisScript onsaleLuaDefaultRedisScript;

    @Autowired
    @Qualifier("redisTemplateLeyou")
    private RedisTemplate redisTemplate;

    @Autowired
    @Qualifier( value = "leyouOnSaleRabbitTemplate" )
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitMQConfiguration rabbitMQConfiguration;

    @Async(value="onSaleThreadPool_OnSaleService")
    public void snapUpOrder(long onSaleProductID, long userID, String hashTag){
        logger.warn("Processing onSaleProductID={}, Thread Name = {}" ,
                onSaleProductID, Thread.currentThread().getName());
        //todo
        //1.帐号是否正常 － 在Service中处理 - JWT token在Zuulfilter中处理，到这里已是正常的(20210330)
        //2.24小时之内是否购买过该商品 - 见下
        //2-1.(Done)同一用户不能购买多次 － lua
        //3.(Done)是否存在未支付秒杀订单 - 见下
        //4.(Done)该秒杀商品是否还有库存 － lua
        //5.该秒杀商品抢购人数是否达到上限
        //6.恶意抢单
        //7.(Done)抢单 - lua

        //todo
        //1.Redis中的和workfow有关Key的过期时间是否应和Tomcat中的Session过期时间相同
        //2.logout时手动删除

        //3.是否存在未支付秒杀订单
        //Set_Order_for_user:{user_id} (expire:<Redis中的和workfow有关Key的过期时间是否应和Tomcat中的Session过期时间相同,logout时手动删除>,加入order时，检查是否有Order过期，并手动Remove)
            // -> Value_Object_OrderCacheDTO_On_OrderUniqueID:'uniqueid'
            // 通过过期监听实现expire (经过研究后发现，过期监听会有delay,不能很精准的触发)
        //Value_Object_OrderCacheDTO_On_OrderUniqueID:'uniqueid' (expire:<Redis中的和workfow有关Key的过期时间是否应和Tomcat中的Session过期时间相同,logout时手动删除>)
            // -> OrderCacheDTO object which include priductid and status
            // 在set value时同时设置expire <Redis中的和workfow有关Key的过期时间是否应和Tomcat中的Session过期时间相同,logout时手动删除>

        //库存
        //2-1.同一用户不能购买多次
        //GOODS:stock:{product_id} (expire:24hours) -> long //运维添加GOODS:stock:{product_id}时，同时设置expire
        //Set_User_for_Product_Purchased:{product_id} (expire:24hours)-> [userID] //在lua中设置expire

        //2.24小时之内是否购买过该商品
        //未实现，和2-1冲突
        // 现在的实现是：1. 同一用户不能购买多次,
        //      （1）如之前"秒杀订单"已支付，"RedisKeyConstants.SET_USER_FOR_PRODUCT_PURCHASED"会挡住购买
        //      （2）尽管"RedisKeyConstants.SET_ORDER_FOR_USER + userI"，
        //          和"RedisKeyConstants.SET_USER_FOR_PRODUCT_PURCHASED"设置24小时过期（可以根据具体秒杀周期设置），但秒杀周期很短，应该一般最多也就一天
        //
        //Hash_Product_for_User_purchased:{user_id} ->
        //key:product_id, value:上次购买时间

        //get OnSaleStatus
        String onSaleStatusKey = RedisKeyConstants.HASH_ONSALESTATUS_BY_PRODUCT_ON_USERID + onSaleProductID + hashTag;
        OnSaleStatus onSaleStatus = (OnSaleStatus)redisTemplate.boundHashOps(onSaleStatusKey).get(String.valueOf(userID));

        //3.是否存在未支付秒杀订单
        //String setOrderForUserKey = RedisKeyConstants.SET_ORDER_FOR_USER + userID; //其实不加userID也可实现
        String setOrderForUserKey = RedisKeyConstants.SET_ORDER_FOR_USER;
        BoundSetOperations setOrderForUser = redisTemplate.boundSetOps(setOrderForUserKey);
        if (!setOrderForUser.members().isEmpty()) {
            for (String orderUniqueIDKey : (Set<String>)setOrderForUser.members()) {
                if (redisTemplate.hasKey(orderUniqueIDKey)) {
                    OrderDtoCache orderDtoCache = (OrderDtoCache)redisTemplate.boundValueOps(orderUniqueIDKey).get();
                    //未支付秒杀订单
                    if (orderDtoCache.getStatus() == 1 && orderDtoCache.getOrderType() == 2) {
                        //返回 不能购买秒杀商品 － reason: 存在未支付秒杀订单
                        onSaleStatus.setStatus(4);
                        onSaleStatus.setReason("存在未支付秒杀订单");
                        redisTemplate.boundHashOps(onSaleStatusKey).put(String.valueOf(userID), onSaleStatus);
                        return;
                    }
                }
            }
        }

        //运行lua
        //2-1.同一用户不能购买多次 － lua
        //4.该秒杀商品是否还有库存 － lua
        //7.抢单 - lua
        //spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本异常，此处拿到原redis的connection执行脚本
        String userPurchasedKey =RedisKeyConstants.SET_USER_FOR_PRODUCT_PURCHASED + onSaleProductID + hashTag;
        String productStockKey = RedisKeyConstants.GOODS_STOCK + onSaleProductID + hashTag;
        List<String> keys = Arrays.asList(userPurchasedKey, productStockKey);
        List<String> args = Arrays.asList(String.valueOf(userID));
        Long result = (Long)redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                // 集群模式和单点模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                // 集群
                if (nativeConnection instanceof JedisCluster) {
                    return (Long) ((JedisCluster) nativeConnection)
                            .eval(onsaleLuaDefaultRedisScript.getScriptAsString(), keys, args);
                }

                // 单点
                else if (nativeConnection instanceof Jedis) {
                    return (Long) ((Jedis) nativeConnection)
                            .eval(onsaleLuaDefaultRedisScript.getScriptAsString(), keys, args);
                }
                return null;
            }
        });

        if (result != null) {
            if (result == 1) {
                System.out.println("success");
                onSaleStatus.setStatus(2);
                redisTemplate.boundHashOps(onSaleStatusKey).put(String.valueOf(userID), onSaleStatus);

                //todo - Done
                //create OrderDTOCache in Redis
                OrderDtoCache orderDtoCache = new OrderDtoCache();
                orderDtoCache.setOnSaleProductID(onSaleStatus.getGoodsId());
                orderDtoCache.setStatus(2);
                orderDtoCache.setOrderType(2);
                orderDtoCache.setOrderUniqueID(onSaleStatus.getUniqueID());
                orderDtoCache.setNum(1);
                orderDtoCache.setCreateDate(onSaleStatus.getCreateTime());

                String orderDtoCacheKey = RedisKeyConstants.VALUE_OBJECT_ORDERCACHEDTO_ON_ORDERUNIQUEID + onSaleStatus.getUniqueID();
                redisTemplate.boundValueOps(orderDtoCacheKey).set(orderDtoCache,1, TimeUnit.DAYS);
                redisTemplate.boundSetOps(setOrderForUserKey).add(orderDtoCacheKey);

                //更新status为"秒杀等待支付"
                onSaleStatus.setStatus(2);
                redisTemplate.boundHashOps(onSaleStatusKey).put(String.valueOf(userID), onSaleStatus);

                //todo - Done
                //传入onSaleProductID和userID
                //put it in delay queue
                Map<String, String> map = new HashMap<String, String>();
                map.put("onSaleProductID", String.valueOf(onSaleProductID));
                map.put("userID", String.valueOf(userID));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = dateFormat.format(new Date());
                map.put("sentTime",currentTime);
                map.put("hashTag",hashTag);

//                amqpTemplate.convertAndSend(rabbitMQConfiguration.DLX_EXCHANGE,
//                        rabbitMQConfiguration.QUEUE_PAYMENT_DELAY,
//                        map);

                amqpTemplate.convertAndSend(rabbitMQConfiguration.QUEUE_PAYMENT_DELAY, map, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        message.getMessageProperties().setExpiration("10000");
                        return message;
                    }
                });


                return;
            } else if (result == 2) {
                System.out.println("only one purchased allowed");
                onSaleStatus.setStatus(4);
                onSaleStatus.setReason("only one purchased allowed");
                redisTemplate.boundHashOps(onSaleStatusKey).put(String.valueOf(userID), onSaleStatus);
            } else if (result == 3) {
                System.out.println("stock is not configured");
                onSaleStatus.setStatus(4);
                onSaleStatus.setReason("stock is not configured");
                throw new LyException(ExceptionEnum.STOCK_NOT_SET_UP_ERROR);
            } else if (result == 4) {
                System.out.println("sold out");
                onSaleStatus.setStatus(4);
                onSaleStatus.setReason("sold out");
                redisTemplate.boundHashOps(onSaleStatusKey).put(String.valueOf(userID), onSaleStatus);
            }
        }

        redisTemplate.exec();
        RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());

        return;
//        return new AsyncResult<String>("hello world !!!!");
    }

}
