package com.leyou.client;

import com.leyou.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "user-service" , contextId="UserClient")
public interface UserClient extends UserApi {
}