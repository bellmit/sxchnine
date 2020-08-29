package com.project.repository;

import com.project.model.User;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final ReactiveRedisTemplate<String, User> reactiveRedisTemplate;

    public UserRepositoryImpl(ReactiveRedisTemplate<String, User> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return reactiveRedisTemplate.opsForValue().get(email);
    }

    @Override
    public Mono<Void> save(User user) {
        return reactiveRedisTemplate.opsForValue().set(user.getEmail(), user)
                .then();
    }

    @Override
    public Mono<Void> deleteUserById(String id) {
        return reactiveRedisTemplate.opsForValue().delete(id)
                .then();
    }

    @Override
    public Mono<Void> deleteUserByEmail(String email) {
        return reactiveRedisTemplate.opsForValue().delete(email)
                .then();
    }
}
