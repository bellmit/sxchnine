package com.project.business;

import com.project.model.Subscription;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@AllArgsConstructor
@Slf4j
public class SubscriptionService {

    private final WebClient webClient;
    private final EmailSubscription emailSubscription;

    public Mono<Void> sendEmailToSubscribers() {
        return webClient.get()
                .uri("/subscription/subscriptions")
                .retrieve()
                .bodyToMono(Subscription.class)
                .doOnError(error -> log.error("cannot get subscriptions", error))
                .flatMap(subscription -> Mono.fromRunnable(() -> emailSubscription.sendEmail(subscription))
                        .doOnError(error -> log.error("cannot send email subscription to users", error)))
                .subscribeOn(Schedulers.boundedElastic())
                .then();

    }

}
