package com.leyou.auth.oauth2.client;

import com.leyou.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "user-service" , contextId="UserClient" , fallback=UserClientFallBack.class)
public interface UserClient extends UserApi {
}
