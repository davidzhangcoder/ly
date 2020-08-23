package com.leyou.page.client;

import com.leyou.api.BrandAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service" , contextId="BrandClient")
public interface BrandClient extends BrandAPI {
}
