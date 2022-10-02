package com.study.jinius.common.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    private String host = "localhost";
    private int port = 6379;

    @Bean
    public StatefulRedisConnection<String, String> statefulRedisConnection() {
        RedisURI redisURI = new RedisURI();
        redisURI.setHost(host);
        redisURI.setPort(port);

        RedisClient redisClient = RedisClient.create(redisURI);

        return redisClient.connect();
    }
}
