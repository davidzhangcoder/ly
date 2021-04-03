package com.leyou.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Conditional(value = JedisConidtion.class)
public class JedisRedisConfig {

    @Bean
    public JedisPoolConfig jedisPool(JedisProperties jedisProperties) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();


        jedisPoolConfig.setMaxTotal(jedisProperties.getMaxActive());
        jedisPoolConfig.setMaxIdle(jedisProperties.getMaxIdle());
        jedisPoolConfig.setMinIdle(jedisProperties.getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(jedisProperties.getMaxWait());
        jedisPoolConfig.setBlockWhenExhausted(true);
        jedisPoolConfig.setTestOnBorrow(true);

        return jedisPoolConfig;
    }

    @Bean
    public RedisClusterConfiguration jedisConfig(JedisProperties jedisProperties) {
        RedisClusterConfiguration config = new RedisClusterConfiguration();

        String nodes = jedisProperties.getNodes();
        int maxRedirects = jedisProperties.getMaxRedirects();

        String[] sub = nodes.split(",");
        List<RedisNode> nodeList = new ArrayList<RedisNode>(sub.length);
        String[] tmp;
        for (String s : sub) {
            tmp = s.split(":");
            // fixme 先不考虑异常配置的case
            nodeList.add(new RedisNode(tmp[0], Integer.valueOf(tmp[1])));
        }

        config.setClusterNodes(nodeList);
        config.setMaxRedirects(maxRedirects);
        //config.setPassword(RedisPassword.of(password));
        return config;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPool,
                                                         RedisClusterConfiguration jedisConfig) {
        JedisConnectionFactory factory = new JedisConnectionFactory(jedisConfig, jedisPool);
        factory.afterPropertiesSet();
        return factory;
    }


    //以下是配主从时用的
    //https://blog.csdn.net/xue15029240296/article/details/96857052
//    public JedisConnectionFactory getRedisConnFactory(RedisProperties redisProperties) {
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
//        jedisConnectionFactory.setDatabase(redisProperties.getDatabase());
//        jedisConnectionFactory.setHostName(redisProperties.getHost());
//        jedisConnectionFactory.setPort(redisProperties.getPort());
//        jedisConnectionFactory.setPassword(redisProperties.getPassword());
//        jedisConnectionFactory.setTimeout(redisProperties.getTimeout());
//
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxIdle(redisProperties.getPool().getMaxIdle());
//        jedisPoolConfig.setMinIdle(redisProperties.getPool().getMinIdle());
//        jedisPoolConfig.setMaxTotal(redisProperties.getPool().getMaxActive());
//        jedisPoolConfig.setMaxWaitMillis(redisProperties.getPool().getMaxWait());
//        jedisPoolConfig.setTestOnBorrow(true);
//
//        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
//
//        return jedisConnectionFactory;
//    }
//
//    public RedisTemplate buildRedisTemplate(RedisConnectionFactory redisConnectionFactory){
//        RedisSerializer redisSerializer = new StringRedisSerializer();
//        RedisTemplate redisTemplate = new RedisTemplate();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        redisTemplate.setKeySerializer(redisSerializer);
//        redisTemplate.setValueSerializer(redisSerializer);
//        redisTemplate.setHashKeySerializer(redisSerializer);
//        redisTemplate.setHashValueSerializer(redisSerializer);
//
//        return redisTemplate;
//    }

}
