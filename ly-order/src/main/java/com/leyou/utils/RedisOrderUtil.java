package com.leyou.utils;

import com.leyou.client.GoodsClient;
import com.leyou.domain.Sku;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisOrderUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private GoodsClient goodsClient;


    public long updateStock(String lockname, String key , long skuId , long quantity){
        //1.获取一把锁，只要名字一样，就是同一把锁
        /**
         * 这里用的是本地的Redis,实际上要做成配置
         */
        RedissonClient redisson = Redisson.create();
        RLock lock = redisson.getLock(lockname);

        //2.加锁和解锁
        try {
            lock.lock();
            System.out.println("加锁成功，执行业务方法..."+Thread.currentThread().getId());

            String stockStr = stringRedisTemplate.boundValueOps(key).get();
            if(StringUtils.isEmpty(stockStr)) {
                //get from DB, and update redis
                List<Sku> skuListByIds = goodsClient.getSKUListByIds(Arrays.asList(skuId));
                if(skuListByIds==null || skuListByIds.size()==0)
                    throw new RuntimeException("获取库存失败");
                Long stockFromDB = skuListByIds.get(0).getStock();
                stockStr = stockFromDB.toString();
                stringRedisTemplate.boundValueOps(key).set(stockStr);
            }

            int stock = Integer.parseInt(stockStr);

            if(stock >= quantity) {
                Long decrement = stringRedisTemplate.boundValueOps(key).decrement(quantity);
                return decrement.longValue();
            }

            return 0;
        } catch (Exception e){

        }finally {
            lock.unlock();
            System.out.println("释放锁..."+Thread.currentThread().getId());
        }

        return 0;
    }

}
