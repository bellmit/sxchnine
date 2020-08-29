package com.project.configuration;

import com.project.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, User> reactiveRedisTemplate(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        Jackson2JsonRedisSerializer<User> valueSerializer = new Jackson2JsonRedisSerializer(User.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, User> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
        RedisSerializationContext<String, User> context =
                builder.value(valueSerializer).build();

        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, context);
    }

}
