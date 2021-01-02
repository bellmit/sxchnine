package com.project.service;

import com.project.model.Subscription;
import com.project.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public Flux<Subscription> getSubscriptions(){
        return subscriptionRepository.findAll();
    }

    public Mono<Subscription> saveSubscription(Subscription subscription){
        return subscriptionRepository.save(subscription);
    }

    public Mono<Boolean> deleteSubscription(String email){
        return subscriptionRepository.deleteSubscriptionByEmail(email);
    }


}
