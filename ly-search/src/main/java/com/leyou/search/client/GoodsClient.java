package com.leyou.search.client;

import com.leyou.api.GoodsAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service" , contextId="GoodsClient")
public interface GoodsClient extends GoodsAPI
{
}
