package com.leyou.service.impl;

import com.leyou.common.cache.OrderDtoCache;
import com.leyou.common.dto.OnSaleStatus;
import com.leyou.common.utils.IdWorker;
import com.leyou.common.utils.RedisKeyConstants;
import com.leyou.configuration.WaitingListConfiguration;
import com.leyou.service.OnSaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service(value="OnSaleServiceImpl")
@Transactional
public class OnSaleServiceImpl implements OnSaleService {

    private Logger logger = LoggerFactory.getLogger(OnSaleServiceImpl.class);

    @Autowired
    private OnSaleAsyncCreater onSaleAsyncCreater;

    @Autowired
    @Qualifier("redisTemplateLeyou")
    private RedisTemplate redisTemplate;

    @Autowired
    @Qualifier( value = "leyouWaitingListRabbitTemplate" )
    private RabbitTemplate amqpTemplate;

    @Autowired
    public WaitingListConfiguration waitingListConfiguration;

    @Override
    public long snapUpOrder(long onSaleProductID , long userID ) {
        IdWorker idWorker = new IdWorker();
        long uniqueID = idWorker.nextId();

        OnSaleStatus onSaleStatus = new OnSaleStatus( userID, Calendar.getInstance(), 1, onSaleProductID,
                "",uniqueID);
        String hashTag = "_{OnSaleServiceImpl_snapUpOrder}";
        String key = RedisKeyConstants.HASH_ONSALESTATUS_BY_PRODUCT_ON_USERID + onSaleProductID + hashTag;
        redisTemplate.boundHashOps(key).put(String.valueOf(userID), onSaleStatus);
        redisTemplate.expire(key, 1, TimeUnit.DAYS);

        onSaleAsyncCreater.snapUpOrder(onSaleProductID, userID, hashTag);

        return uniqueID;
    }

    @Override
    public long snapUpOrderByUsingRedis(long onSaleProductID, long userID) {

            IdWorker idWorker = new IdWorker();
        long uniqueID = idWorker.nextId();
        OnSaleStatus onSaleStatus = new OnSaleStatus( userID, Calendar.getInstance(), 1, onSaleProductID,"",uniqueID);

        String hashTag = "_{OnSaleServiceImpl_snapUpOrder}";
        String key = RedisKeyConstants.LIST_ONSALESTATUS_WAITINGLIST + hashTag;

        redisTemplate.boundListOps(key).leftPush(onSaleStatus);

        return uniqueID;
    }

    @Override
    public long snapUpOrderByUsingRabbitmq(long onSaleProductID, long userID) {

        IdWorker idWorker = new IdWorker();
        long uniqueID = idWorker.nextId();
        OnSaleStatus onSaleStatus = new OnSaleStatus( userID, Calendar.getInstance(), 1, onSaleProductID,"",uniqueID);

        String hashTag = "_{OnSaleServiceImpl_snapUpOrder}";
        String onSaleStatusKey = RedisKeyConstants.HASH_ONSALESTATUS + hashTag;
        List<OnSaleStatus> onSaleStatusList = (List<OnSaleStatus>) redisTemplate.boundHashOps(onSaleStatusKey).get(String.valueOf(userID));
        if( onSaleStatusList == null )
            onSaleStatusList = new ArrayList<OnSaleStatus>();
        onSaleStatusList.add( onSaleStatus );
        redisTemplate.boundHashOps(onSaleStatusKey).put(String.valueOf(userID), onSaleStatusList);

        CorrelationData correlationData = new CorrelationData( uniqueID + "" );
        amqpTemplate.convertAndSend( waitingListConfiguration.getExchange() ,waitingListConfiguration.getRoutingKey() , onSaleStatus, correlationData);

//        amqpTemplate.convertAndSend( waitingListConfiguration.getExchange() ,waitingListConfiguration.getRoutingKey() , onSaleStatus);

        //测试代码
//        amqpTemplate.convertAndSend("notexistingexchange" ,"notexistingrouteringkey" , onSaleStatus, correlationData);

        return uniqueID;
    }

    @Override
    public int queryOnSaleStatus(long userID, long uniqueID) {
        String hashTag = "_{OnSaleServiceImpl_snapUpOrder}";
        String onSaleStatusKey = RedisKeyConstants.HASH_ONSALESTATUS + hashTag;
        List<OnSaleStatus> onSaleStatusList = (List<OnSaleStatus>) redisTemplate.boundHashOps(onSaleStatusKey).get(String.valueOf(userID));
        if( onSaleStatusList != null ) {
            for (OnSaleStatus saleStatus : onSaleStatusList) {
                if (saleStatus.getUniqueID() == uniqueID) {
                    return saleStatus.getStatus();
                }
            }
        }

        return 0;
    }
}

//long uniqueID = idWorker.nextId();的算法有问题，会得到相同的ID
//        2021-04-04 16:19:18.255  WARN [onsale-service,7698a45d2a3b366a,a828323ddb6e79a3,true] 853 --- [cTaskExecutor-1] nSaleAsyncCreaterByUsingRedisAndRabbitMQ : Processing onSaleProductID=10781492357, Thread Name = SimpleAsyncTaskExecutor-1
//        success: 1378804389968343041
//        消费者: ACK , Channel Number: 2 , 1378804389968343041
//        2021-04-04 16:19:18.289  WARN [onsale-service,29f3168ff87a9fa8,a38163bc57caa190,true] 853 --- [cTaskExecutor-1] nSaleAsyncCreaterByUsingRedisAndRabbitMQ : Processing onSaleProductID=10781492357, Thread Name = SimpleAsyncTaskExecutor-1
//        success: 1378804389968343041
//        消费者: ACK , Channel Number: 2 , 1378804389968343041
//        WaitingListConfirmCallBack - 到达exchange - ack:成功 correlationData:id(OnSaleStatus's uniqueID)=1378804390400356352
//        2021-04-04 16:19:18.332  WARN [onsale-service,573d424c90b96398,d2245e7cd61d6a9d,true] 853 --- [cTaskExecutor-1] nSaleAsyncCreaterByUsingRedisAndRabbitMQ : Processing onSaleProductID=10781492357, Thread Name = SimpleAsyncTaskExecutor-1
//        success: 1378804389968343041
//        消费者: ACK , Channel Number: 2 , 1378804389968343041

