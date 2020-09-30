package com.leyou.service.impl;

import com.leyou.common.dto.CartDto;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.dao.*;
import com.leyou.domain.*;
import com.leyou.service.GoodsService;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
            //减库存
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
        Stock stockBySkuId = stockDao.getStockBySkuId(skuID);
        return stockBySkuId!=null&&stockBySkuId.getStock()!=null?stockBySkuId.getStock().longValue():0;
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
