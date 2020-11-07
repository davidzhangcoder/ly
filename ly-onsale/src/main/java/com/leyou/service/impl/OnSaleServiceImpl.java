package com.leyou.service.impl;

import com.leyou.common.dto.OnSaleStatus;
import com.leyou.common.utils.IdWorker;
import com.leyou.common.utils.RedisKeyConstants;
import com.leyou.service.OnSaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Service(value="OnSaleServiceImpl")
@Transactional
public class OnSaleServiceImpl implements OnSaleService {

    private Logger logger = LoggerFactory.getLogger(OnSaleServiceImpl.class);

    @Autowired
    private OnSaleAsyncCreater onSaleAsyncCreater;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public long snapUpOrder(long onSaleProductID) {
        long userID = 1;
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
    public void queryOnSaleStatus(long uniqueID) {

    }
}
