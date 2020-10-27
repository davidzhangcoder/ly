package com.leyou.configuration;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

@Configuration
@Conditional(value= RedissonCondition.class)
public class RedissonConfig {

    private Logger log = LoggerFactory.getLogger(RedissonConfig.class);

    @Bean
    @ConditionalOnProperty(name = "spring.redisson.cluster", havingValue = "false")
    public RedissonClient getSingleRedisson() {
        Config config = new Config();
        RedissonProperties redissonProperties = getRedissonProperties();

        String nodes = redissonProperties.getNodes();
        if(StringUtils.isEmpty(nodes)) {
            throw new RuntimeException("nodes is not allow empty! please check your redis config");
        }

        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress("redis://" + nodes)
                .setTimeout(redissonProperties.getTimeout())
                .setConnectionPoolSize(redissonProperties.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize());

        if(StringUtils.isNotBlank(redissonProperties.getPassword())) {
            serverConfig.setPassword(redissonProperties.getPassword());
        }

        RedissonClient redissonClient = Redisson.create(config);
        log.info("init redisson success .... ");

        return redissonClient;
    }

    @Bean
    public RedissonProperties getRedissonProperties() {
        return new RedissonProperties();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.redisson.cluster", havingValue = "true")
    public RedissonClient getClusterRedisson(){

        Config config = new Config();
        RedissonProperties redissonProperties = getRedissonProperties();

        String[] nodes = redissonProperties.getNodes().split(",");

        if (ObjectUtils.isEmpty(nodes)) {
            throw new RuntimeException("nodes is not allow empty! please check your redis config");
        }

        for(int i=0;i<nodes.length;i++){
            nodes[i] = "redis://"+nodes[i];
        }

        ClusterServersConfig clusterServersConfig = config.useClusterServers()
                .setScanInterval(2000)
                .setTimeout(redissonProperties.getTimeout())
                .addNodeAddress(nodes);

        if(StringUtils.isNotBlank(redissonProperties.getPassword())) {
            clusterServersConfig.setPassword(redissonProperties.getPassword());
        }

        RedissonClient redisson = Redisson.create(config);

        log.info("init redisson cluster success .... ");
        return redisson;
    }

}
