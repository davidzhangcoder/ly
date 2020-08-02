package com.leyou.search.client;

import com.leyou.api.SpecificationAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service" , contextId="SpecificationClient")
public interface SpecificationClient extends SpecificationAPI {
}
