package com.leyou.service.impl;

import com.leyou.common.dto.OnSaleStatus;
import com.leyou.common.utils.RedisKeyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.plaf.TableHeaderUI;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.LockSupport;

@Component
public class SnapOrderHandlerByUsingRedis {

    @Autowired
    @Qualifier("onSaleThreadPool_OnSaleService")
    public Executor onSaleThreadPool;

    @Autowired
    @Qualifier("redisTemplateLeyou")
    private RedisTemplate redisTemplate;

    @Autowired
    private OnSaleAsyncCreaterByUsingRedis onSaleAsyncCreater;

    @PostConstruct
    @Async
    public void handleSnapOrder(){
        String hashTag = "_{OnSaleServiceImpl_snapUpOrder}";
        String key = RedisKeyConstants.LIST_ONSALESTATUS_WAITINGLIST + hashTag;

        for (int i = 0; i < 3 ; i++) {
            Thread thread = new Thread(() -> {
                while( true ) {
                    OnSaleStatus onSaleStatus = (OnSaleStatus) redisTemplate.boundListOps(key).rightPop();
                    if (onSaleStatus != null) {
//                        long userID = onSaleStatus.getUserID();
//                        Long onSaleProductID = onSaleStatus.getGoodsId();

                        onSaleAsyncCreater.snapUpOrder(onSaleStatus);
                    }

                    RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
                }
            });

            onSaleThreadPool.execute( thread );
        }

    }
}
