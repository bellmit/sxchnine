package com.project.service;

import com.project.model.Subscription;
import com.project.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public Flux<Subscription> getSubscriptions(){
        return subscriptionRepository.findAll();
    }

    public Mono<Subscription> saveSubscription(Subscription subscription){
        log.info("save subscription: {}", subscription.getEmail());
        return subscriptionRepository.save(subscription);
    }

    public Mono<Boolean> deleteSubscription(String email){
        log.info("delete subscription: {}", email);
        return subscriptionRepository.deleteSubscriptionByEmail(email);
    }


}
