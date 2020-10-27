package com.leyou.service.impl;

import com.leyou.common.dto.CartDto;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.RedisKeyConstants;
import com.leyou.common.vo.PageResult;
import com.leyou.dao.*;
import com.leyou.domain.*;
import com.leyou.service.GoodsService;
//import org.redisson.Redisson;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service( value = "GoodsServiceImpl" )
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SkuDao skuDao;

    @Autowired
    private SpuDetailDao spuDetailDao;

    @Autowired
    private SpuDao spuDao;

    @Autowired
    private StockDao stockDao;

    @Autowired
    private Account1Dao account1Dao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private RedissonClient redisson;

    @Override
    public List<Sku> getSKUBySPUId(long spuid) {
        return skuDao.findSkuBySpuId(spuid);
    }

    @Override
    public SpuDetail getSPUDetailBySPUId(long spuid) {
        return spuDetailDao.findBySpuId(spuid);
    }

    @Override
    public PageResult getSpuByPage(boolean descending, int page, int rowsPerPage, String sortBy) {
        page--;
        page = page < 0 ? 0 : page;//page 为页码，数据库从0页开始
        //可以使用重载的 of(int page, int size, Sort sort) 方法指定排序字段
        //Pageable pageable = PageRequest.of((int)page, (int)rowsPerPage);

        // Order定义了排序规则。参数一是根据倒叙还是正序，参数二是根据那个字段进行排序。
        Sort.Order order = new Sort.Order((descending?Sort.Direction.DESC:Sort.Direction.ASC), sortBy);
        // Sort对象封装了排序的规则。
        Sort sort = Sort.by(order);
        Pageable pageable = PageRequest.of(page, rowsPerPage , sort );

        Page<Spu> spuPage = spuDao.findAll( pageable );

        PageResult<Brand> pageResult = new PageResult(spuPage.getTotalElements(), (long)spuPage.getTotalPages(), spuPage.getContent());

        return pageResult;
    }

    @Override
    public Spu querySpuById(Long id) {

        return spuDao.findById( id ).orElse( null );

    }

    @Override
    public Spu testPersistSpu(long id, String title) {
        Spu spu = spuDao.getOne(id);
        spu.setTitle( spu.getTitle() + " / 测试" + title);

        spu = spuDao.save( spu );

        //todo: using AOP to send message here?

        return spu;
    }

    @Override
    public List<Sku> getSKUListByIds(List<Long> skuIds) {
        List<Sku> skusWithStockBySkuIDs = skuDao.getSkusWithStockBySkuIDs(skuIds);

        for (Sku sku : skusWithStockBySkuIDs) {
            Stock stock = stockDao.findById(sku.getId()).orElse(null);
            if(stock != null) {
                sku.setStock(stock.getStock());
            }
        }


        return skusWithStockBySkuIDs;
    }

    @Override
    public void decreaseStock(List<CartDto> cartDtos) {
        for (CartDto cartDto : cartDtos) {
            stockDao.lockStockBySkuId(cartDto.getSkuId());
            //1.删除缓存
            //必须先删除缓存，以保证数据一致性
            //如果先减数据库库存，再删除缓存，如果删除缓存失败，就会产生数据不一致
            //如果先删除缓存,再减数据库库存,如果减数据库库存失败,读取时缓存不存在，会从数据库获取
            String skuKey = RedisKeyConstants.GOODS_STOCK+cartDto.getSkuId();
            if( redisTemplate.hasKey(skuKey) )
                redisTemplate.delete(skuKey);

            //2.减库存
            int count =  stockDao.decreaseStock(cartDto.getSkuId(),cartDto.getNum());
            if (count <= 0){
                throw new LyException(ExceptionEnum.STOCK_NOT_ENOUGH);
            }
        }
    }

    @Override
    @Transactional(timeout = 2)
    public void testFallBack(long id) {

        Account1 account1 = new Account1();
        account1.setAmount(1);
        account1Dao.save(account1);

//        if(id<=0) {
//            try {
//                TimeUnit.MILLISECONDS.sleep(4000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
////            int a = 1/0;
//        }

        Account1 account11 = new Account1();
        account11.setAmount(11);
        account1Dao.save(account11);


        if(id<=0) {
            try {
                TimeUnit.MILLISECONDS.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            int a = 1/0;
        }
    }

    @Override
    public long getStockBySkuId(long skuID) {

        String skuKey =RedisKeyConstants.GOODS_STOCK+skuID;
        Long o = getStockbySkuidFromCache(skuID);
        if (o != null) return o;

        //1.获取一把锁，只要名字一样，就是同一把锁
        /**
         * 这里用的是本地的Redis,实际上要做成配置
         */
        RLock lock = redisson.getLock(skuKey);

        long stock = 0;
        //2.加锁和解锁
        try {
            lock.lock(); //the thread would hang in here when it didn't get the lock
            //System.out.println("加锁成功，执行业务方法..."+Thread.currentThread().getId());

            Long stockFromCache = getStockbySkuidFromCache(skuID);
            if (stockFromCache != null) return stockFromCache;

            Stock stockBySkuId = stockDao.getStockBySkuId(skuID);
            stock = stockBySkuId != null && stockBySkuId.getStock() != null ? stockBySkuId.getStock().longValue() : 0;

            redisTemplate.opsForValue().set(skuKey, stock);

        } catch (Exception e){
            throw new LyException(ExceptionEnum.STOCK_RETRIEVE_ERROR);
        }finally {
            lock.unlock();
            //System.out.println("释放锁..."+Thread.currentThread().getId());
        }
        return stock;
    }

    private Long getStockbySkuidFromCache(long skuID) {
        String skuKey =RedisKeyConstants.GOODS_STOCK+skuID;
        if (redisTemplate.hasKey(skuKey) ) {
            Object o = redisTemplate.opsForValue().get(skuKey);
            if( o != null && o instanceof Long) {
                return ((Long)o).longValue();
            }
        }
        return null;
    }

}

//    结论
//
//            写道
//Spring事务超时 = 事务开始时到最后一个Statement创建时时间 + 最后一个Statement的执行时超时时间（即其queryTimeout）。
//        4、因此
//
//        假设事务超时时间设置为2秒；假设sql执行时间为1秒；
//
//        如下调用是事务不超时的
//
//public void testTimeout() throws InterruptedException {
//        System.out.println(System.currentTimeMillis());
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
//        jdbcTemplate.execute(" update test set hobby = hobby || '1'");
//        System.out.println(System.currentTimeMillis());
//        Thread.sleep(3000L);
//        }
//        而如下事务超时是起作用的；
//
//public void testTimeout() throws InterruptedException {
//        Thread.sleep(3000L);
//        System.out.println(System.currentTimeMillis());
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
//        jdbcTemplate.execute(" update test set hobby = hobby || '1'");
//        System.out.println(System.currentTimeMillis());
//        }
//        因此，不要忽略应用中如远程调用产生的事务时间和这个事务时间是否对您的事务产生影响。
//
//        另外：
//
//        1、事务超时不起作用，您要首先检查您的事务起作用了没：可以参考使用Aop工具类诊断常见问题
//
//        2、如果您用的JPA，且spring版本低于3.0，可能您的事务超时不起作用：https://jira.springsource.org/browse/SPR-5195
//
//        3、如果您用JDBC，但没有用JdbcTemplate，直接使用DateSourceUtils进行事务控制时，要么自己设置Statement的queryTimeout超时时间，要么使用TransactionAwareDataSourceProxy，其在创建Statement时会自动设置其queryTimeout。
