package com.project.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory connectionFactory(){
        return new LettuceConnectionFactory("localhost", 9000);
    }

    @Bean
    public RedisTemplate redisTemplate(){
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<byte[], byte[]>();
        redisTemplate.setConnectionFactory(connectionFactory());
        return redisTemplate;
    }
}
