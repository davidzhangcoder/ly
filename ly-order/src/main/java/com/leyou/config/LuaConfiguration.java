package com.leyou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration(value="LuaForOnOrder")
public class LuaConfiguration {

    @Bean(name="onsale")
    public DefaultRedisScript<Long> redisScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        //The script result type. Should be one of Long, Boolean, List, or deserialized value type
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/onsale.lua")));
        return redisScript;
    }

    @Bean(name="onsaleTestSame")
    public DefaultRedisScript<String> redisScriptTestSame() {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(String.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/onsale.lua")));
        return redisScript;
    }


}
