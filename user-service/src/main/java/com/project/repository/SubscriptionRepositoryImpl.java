package com.project.repository;

import com.project.model.Subscription;
import com.project.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private static final String redisHash = "subscriptions";

    private final ReactiveRedisTemplate<String, Subscription> subscriptionReactiveRedisTemplate;

    @Override
    public Mono<Subscription> save(Subscription subscription) {
        return subscriptionReactiveRedisTemplate.opsForHash()
                .put(redisHash, subscription.getEmail().toLowerCase(), subscription)
                .doOnError(error -> log.error("error occurred when saving subscriber {}", subscription.getEmail(), error))
                .thenReturn(subscription);
    }

    @Override
    public Mono<Boolean> deleteSubscriptionByEmail(String email) {
        return subscriptionReactiveRedisTemplate.opsForHash().delete(email);
    }

    @Override
    public Flux<Subscription> findAll() {
        return subscriptionReactiveRedisTemplate.opsForHash()
                .values(redisHash)
                .map(o -> (Subscription)o)
                .doOnError(error -> log.error("error occurred when retrieving subscribers", error))
        ;
    }
}
