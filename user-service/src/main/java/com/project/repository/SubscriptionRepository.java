package com.project.repository;

import com.project.model.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SubscriptionRepository {

    Mono<Subscription> save(Subscription subscription);

    Mono<Boolean> deleteSubscriptionByEmail(String email);

    Flux<Subscription> findAll();

}
