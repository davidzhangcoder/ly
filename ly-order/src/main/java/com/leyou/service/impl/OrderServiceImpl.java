package com.leyou.service.impl;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.client.GoodsClient;
import com.leyou.common.dto.CartDto;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.IdWorker;
import com.leyou.common.utils.RedisKeyConstants;
import com.leyou.dao.OrderDao;
import com.leyou.dao.OrderDetailDao;
import com.leyou.dao.OrderStatusDao;
import com.leyou.domain.Sku;
import com.leyou.dto.OrderDto;
import com.leyou.enums.OrderStatusEnum;
import com.leyou.interceptor.UserInterceptor;
import com.leyou.pojo.Order;
import com.leyou.pojo.OrderDetail;
import com.leyou.pojo.OrderStatus;
import com.leyou.service.OrderService;
import com.leyou.utils.RedisUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service(value="OrderServiceImpl")
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private OrderStatusDao orderStatusDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger(UserInterceptor.class);

    @Override
    @GlobalTransactional
    public Long createOrder(OrderDto orderDto) {

        //1.新增订单
        Order order = new Order();
        //1.1 订单编号  基本信息
        long orderId = idWorker.nextId();
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setPaymentType(orderDto.getPaymentType());

        //1.2 用户信息
        UserInfo user = UserInterceptor.getUserInfo();
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        order.setBuyerRate(false);

//        //1.3 收货人地址
//        AddressDTO addr = AddressClient.findById(orderDto.getAddressId());
//        order.setReceiver(addr.getName());
//        order.setReceiverAddress(addr.getAddress());
//        order.setReceiverDistrict(addr.getDistrict());
//        order.setReceiverCity(addr.getCity());
//        order.setReceiverState(addr.getState());
//        order.setReceiverMobile(addr.getPhone());
//        order.setReceiverZip(addr.getZipCode());

        //1.4 金额
        //把cartdto转为一个map，key是skuId,value是num
        Map<Long, Integer> numMap = orderDto.getCarts().stream()
                .collect(Collectors.toMap(CartDto::getSkuId, CartDto::getNum));
        //获取所有sku的id
        Set<Long> ids = numMap.keySet();
        //根据id查询sku
        List<Sku> skus = goodsClient.getSKUListByIds(new ArrayList<>(ids));


        Set<Long> holdSkuQuantity = new HashSet<>();
        boolean isHoldSuccess =true;
        for (Sku sku : skus) {

            String skuKey =RedisKeyConstants.GOODS_STOCK+sku.getId();

            BoundValueOperations<String, String> skuOperations = stringRedisTemplate.boundValueOps(skuKey);
            if (stringRedisTemplate.hasKey(skuKey)) {
                long stockBySkuId = goodsClient.getStockBySkuId(sku.getId().longValue());
                skuOperations.setIfAbsent(String.valueOf(stockBySkuId));
            }

            Long remainingQuantity = skuOperations.decrement(numMap.get(sku.getId()));
            if(remainingQuantity!= null && remainingQuantity<0) {
                isHoldSuccess = false;
                break;
            }
            holdSkuQuantity.add(sku.getId());

        }

        if( !isHoldSuccess ){
            //roll back quantity
            //throw exception
            for (Long skuID : holdSkuQuantity) {
                String skuKey =RedisKeyConstants.GOODS_STOCK+skuID;
                BoundValueOperations<String, String> skuOperations = stringRedisTemplate.boundValueOps(skuKey);
                skuOperations.increment(numMap.get(skuID));
            }
            throw new LyException(ExceptionEnum.STOCK_NOT_ENOUGH);
        }


        //准备orderDtail集合
        List<OrderDetail> details = new ArrayList<>();

        Long totalPay = 0L;
        for (Sku sku : skus) {
            totalPay = sku.getPrice() * numMap.get(sku.getId());

            //封装orderDtail
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(orderId);
            detail.setImage(StringUtils.substringBefore(sku.getImages(),","));
            detail.setNum(numMap.get(sku.getId()));
            detail.setOwnSpec(sku.getOwnSpec());
            detail.setSkuId(sku.getId());
            detail.setTitle(sku.getTitle());
            detail.setPrice(sku.getPrice().longValue());
            details.add(detail);
        }

        order.setTotalPay(totalPay);
        //实付金额： 总金额 + 邮费 - 优惠金额
        order.setPostFee(0L);
        order.setActualPay(totalPay + order.getPostFee() - 0);

        //1.5 写入数据库
        orderDao.save(order);
//        int count =  orderMapper.insertSelective(order);
//        if (count != 1){
//            logger.error("[创建订单服务order] 创建订单失败,orderId:{}",orderId);
//            throw new LyException(ExceptionEnum.CREATE_ORDER_ERROR);
//        }


        //2.新增订单详情
        orderDetailDao.saveAll(details);
//        count = detailMapper.insertList(details);
//
//        if (count != details.size()){
//            logger.error("[创建订单服务detail] 创建订单失败,orderId:{}",orderId);
//            throw new LyException(ExceptionEnum.CREATE_ORDER_ERROR);
//        }


        //3.新增订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setCreateTime(order.getCreateTime());
        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(OrderStatusEnum.UN_PAY.value());
        orderStatusDao.save(orderStatus);

//        count =  statusMapper.insertSelective(orderStatus);
//        if (count != 1){
//            logger.error("[创建订单服务status] 创建订单失败,orderId:{}",orderId);
//            throw new LyException(ExceptionEnum.CREATE_ORDER_ERROR);
//        }

        //4.减库存   采用同步，在数据库判断
        List<CartDto> cartDtos = orderDto.getCarts();
        goodsClient.decreaseStock(cartDtos);

        System.out.println(orderId);
        return orderId;
    }

    @Override
    public void testMethod() {
//        stringRedisTemplate.setEnableTransactionSupport(true);
//
//        BoundValueOperations<String, String> key1 = stringRedisTemplate.boundValueOps("key1");
//        key1.set("BBBBB");
//        key1.get();
//
//        int a = 1/0;

        SessionCallback<Object> callback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                Object o1 = operations.opsForValue().get("key1");
                operations.opsForValue().set("key1", "AAAAA");
                Object o2 = operations.opsForValue().get("key1");
                operations.discard();
                return operations.exec();
            }
        };
        System.out.println(stringRedisTemplate.execute(callback));

    }

    @Override
    public void orderTestFallBack(long id) {
        goodsClient.testFallBack(id);
    }

}