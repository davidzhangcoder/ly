package com.leyou.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration(value="LuaForCommon")
public class LuaConfiguration {
    @Bean(name="accessLimit")
    public DefaultRedisScript<Long> redisScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        //The script result type. Should be one of Long, Boolean, List, or deserialized value type
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/accesslimit.lua")));
        return redisScript;
    }
}
