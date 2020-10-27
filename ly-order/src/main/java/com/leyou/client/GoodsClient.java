package com.leyou.client;

import com.leyou.api.GoodsAPI;
import com.leyou.client.fallback.GoodsClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;

@FeignClient(value = "item-service" , contextId="GoodsClient"/*, fallback = GoodsClientFallBack.class*/)
@Primary
public interface GoodsClient extends GoodsAPI
{
}
