package com.leyou.page.client;

import com.leyou.api.CategoryAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service" , contextId="CategoryClient")
public interface CategoryClient extends CategoryAPI {
}
