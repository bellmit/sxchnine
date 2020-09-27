package com.project.repository;

import com.project.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private static final String redisHash = "users";

    private final ReactiveRedisTemplate<String, User> reactiveRedisTemplate;

    public UserRepositoryImpl(ReactiveRedisTemplate<String, User> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return reactiveRedisTemplate
                .opsForHash()
                .get(redisHash, email)
                .cast(User.class);
    }

    @Override
    public Mono<User> save(User user) {
        return reactiveRedisTemplate.opsForHash().put(redisHash, user.getEmail(), user)
                .thenReturn(user);
    }

    @Override
    public Mono<Void> deleteUserById(String id) {
        return reactiveRedisTemplate.opsForHash().remove(redisHash, id)
                .then();
    }

    @Override
    public Mono<Void> deleteUserByEmail(String email) {
        return reactiveRedisTemplate.opsForHash().remove(redisHash, email)
                .then();
    }
}
