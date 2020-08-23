package com.leyou.page.client;

import com.leyou.api.SpecificationAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service" , contextId="SpecificationClient")
public interface SpecificationClient extends SpecificationAPI {
}
