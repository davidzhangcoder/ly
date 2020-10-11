package com.leyou.cart.service.impl;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.interceptor.UserInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.utils.RedisKeyConstants;
import com.leyou.cart.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service( value = "CartServiceImpl" )
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    private static String KEY_PREFIX = "CART:USER:ID";

    @Override
    public void addCart(Cart cart) {

        //test
        //String stockStr = stringRedisTemplate.boundValueOps(RedisKeyConstants.GOODS_STOCK + "10781492357" ).get();

        UserInfo userInfo = UserInterceptor.getUserInfo();

        String key = KEY_PREFIX + userInfo.getId();
        String hashKey = cart.getSkuId().toString();

        BoundHashOperations<String, Object, Object> operation = stringRedisTemplate.boundHashOps(key);

        if (operation.hasKey(hashKey)) {
            String jsonCart = operation.get(hashKey).toString();
            Cart cacheCart = JsonUtils.parse(jsonCart, Cart.class);
            cacheCart.setNum( cacheCart.getNum() + cart.getNum() );
            operation.put(hashKey, JsonUtils.serialize(cacheCart));
        }
        else {
            operation.put(hashKey, JsonUtils.serialize(cart));
        }
    }

    @Override
    public String addCartWithoutLogin(Cart cart, String tempCartID) {

        String uniqueID = tempCartID;

        if (redisTemplate.hasKey(RedisKeyConstants.TEMP_CART_ID + uniqueID)) {
            BoundHashOperations hashOperations = redisTemplate.boundHashOps(RedisKeyConstants.TEMP_CART_ID + uniqueID);
            String hashKey = cart.getSkuId().toString();
            if (hashOperations.hasKey(hashKey)) {
                Cart cartFromCache = (Cart)hashOperations.get(hashKey);
                cartFromCache.setNum( cartFromCache.getNum() + cart.getNum() );
                hashOperations.put(hashKey, cartFromCache);
            } else {
                hashOperations.put(hashKey, cart);
            }
        }
        else {
            uniqueID = String.valueOf(redisUtil.getUniqueID());
            BoundHashOperations hashOperations = redisTemplate.boundHashOps(RedisKeyConstants.TEMP_CART_ID + uniqueID);
            String hashKey = cart.getSkuId().toString();
            hashOperations.put(hashKey, cart);
        }

        return uniqueID;
    }

    @Override
    public List<Cart> queryCartList() {
        UserInfo userInfo = UserInterceptor.getUserInfo();

        String key = KEY_PREFIX + userInfo.getId();

        if(!stringRedisTemplate.hasKey(key)) {
            return Collections.emptyList();
        }

        BoundHashOperations<String, Object, Object> operation = stringRedisTemplate.boundHashOps(key);

        List<Cart> retList = operation.values().stream().map(a -> JsonUtils.parse((String) a, Cart.class)).collect(Collectors.toList());
        return retList;
    }

}
