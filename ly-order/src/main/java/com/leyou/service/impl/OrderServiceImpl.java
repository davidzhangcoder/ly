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
import com.leyou.utils.RedisOrderUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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
    private RedisOrderUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    public Long createOrder_FallBack(OrderDto orderDto, UserInfo user, long start,  Throwable e) {
        logger.info("createOrder_FallBack: 订单生成错误");
        System.out.println("createOrder_FallBack: 订单生成错误 " + e );
        //e.printStackTrace();
        throw new LyException(ExceptionEnum.ORDER_CREATE_ERROR);
    }

    @Override
    @GlobalTransactional
//    @HystrixCommand(fallbackMethod = "createOrder_FallBack",
//            commandProperties = {
//                    @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="5000"),//已经在application.yml中配置
//                    @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),// 是否开启断路器
//                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),// 请求次数 默认20个, The default rolling window is 10 seconds
//                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "3000"), // 时间窗口期,that we will sleep before trying again after tripping the circuit
//                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "50"),// 失败率达到多少后跳闸, must be >= 50%
//
//            })
    public Long createOrder(OrderDto orderDto, UserInfo user, long start) throws Exception {

        //{"paymentType":1,"carts":[{"skuId":10781492357,"num":6}],"addressId":1}

        long enterTime = System.currentTimeMillis();
        //logger.warn("start={} , enterTime={}, diff={}", start, enterTime, (enterTime-start) );

        //1.新增订单
        Order order = new Order();
        //1.1 订单编号  基本信息
        long orderId = idWorker.nextId();
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setPaymentType(orderDto.getPaymentType());

        //1.2 用户信息
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

        long start1 = System.currentTimeMillis();
        //根据id查询sku
        List<Sku> skus = goodsClient.getSKUListByIds(new ArrayList<>(ids));
        long end1 = System.currentTimeMillis();
        //System.out.println("goodsClient.getSKUListByIds: " + (end1-start1));


        Set<Long> holdSkuQuantity = new HashSet<>();
        boolean isHoldSuccess =true;
        for (Sku sku : skus) {

            String skuKey =RedisKeyConstants.GOODS_STOCK+sku.getId();
            
            BoundValueOperations<String, String> skuOperations = stringRedisTemplate.boundValueOps(skuKey);
            if (!stringRedisTemplate.hasKey(skuKey)) {
                long start2 = System.currentTimeMillis();
                long stockBySkuId = goodsClient.getStockBySkuId(sku.getId().longValue());
                long end2 = System.currentTimeMillis();
                //System.out.println("goodsClient.getStockBySkuId: " + (end2-start2));

                skuOperations.setIfAbsent(String.valueOf(stockBySkuId));
            }

            Long remainingQuantity = skuOperations.decrement(numMap.get(sku.getId()));
            holdSkuQuantity.add(sku.getId());
            if(remainingQuantity!= null && remainingQuantity<0) {
                isHoldSuccess = false;
                break;
            }
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
        long start3 = System.currentTimeMillis();
        orderDao.save(order);
        long end3 = System.currentTimeMillis();
        //System.out.println("orderDao.save(order): " + (end3-start3));

//        int count =  orderMapper.insertSelective(order);
//        if (count != 1){
//            logger.error("[创建订单服务order] 创建订单失败,orderId:{}",orderId);
//            throw new LyException(ExceptionEnum.CREATE_ORDER_ERROR);
//        }

        //TimeUnit.SECONDS.sleep(4);

        //2.新增订单详情
        long start4 = System.currentTimeMillis();
        orderDetailDao.saveAll(details);
        long end4 = System.currentTimeMillis();
        //System.out.println("orderDetailDao.saveAll(details): " + (end4-start4));

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
        long start5 = System.currentTimeMillis();
        orderStatusDao.save(orderStatus);
        long end5 = System.currentTimeMillis();
        //System.out.println("orderStatusDao.save(orderStatus): " + (end5-start5));

//        count =  statusMapper.insertSelective(orderStatus);
//        if (count != 1){
//            logger.error("[创建订单服务status] 创建订单失败,orderId:{}",orderId);
//            throw new LyException(ExceptionEnum.CREATE_ORDER_ERROR);
//        }

        //4.减库存   采用同步，在数据库判断
        List<CartDto> cartDtos = orderDto.getCarts();
        long start6 = System.currentTimeMillis();
        goodsClient.decreaseStock(cartDtos);
        long end6 = System.currentTimeMillis();
        //System.out.println("goodsClient.decreaseStock(cartDtos): " + (end6-start6));

        //System.out.println(orderId);

//            TimeUnit.SECONDS.sleep(8);

//        System.out.println("after sleep!");

        //test
        orderId = (enterTime-start);

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
