package com.project.controller;

import com.project.model.Subscription;
import com.project.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/subscriptions")
    public Flux<Subscription> subscriptions(){
        return subscriptionService.getSubscriptions();
    }

    @PostMapping("/save")
    public Mono<Subscription> save(@RequestBody Subscription subscription){
        return subscriptionService.saveSubscription(subscription);
    }
}
