package com.leyou.service.impl;

import com.leyou.common.utils.IdWorker;
import com.leyou.domain.Brand;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EhcacheTest {

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void execute() {
        // 获取商户申请缓存容器
        Cache cache = cacheManager.getCache("merchant-apply-cache");
        Brand merchant = new Brand();
        Long id = 1l;
        merchant.setId(id);
        merchant.setName("缓存测试");
        Element element = new Element(id,merchant);
        cache.put(element);
        // 将商户申请数据添加至缓存中 // key : id value : object
//        cache.put(id, merchant);
        // 获取商户申请数据
//        // 方法1
//        Merchant cacheMerchant1 = (Merchant) cache.get(id).get();
//        System.out.println(cacheMerchant1.getName());
//        // 方法2
//        Merchant cacheMerchant2 = cache.get(id, Merchant.class);
//        System.out.println(cacheMerchant2.getName());
//        // 将商户申请数据从缓存中移除
//        cache.evict(id);
    }


}
