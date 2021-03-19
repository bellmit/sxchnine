package com.project.business;

import com.project.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdatesService {

    private final WebClient webClient;
    private final EmailUpdatesSender emailUpdatesSender;

    public Mono<Void> sendEmailUpdatesToAllUsers() {
        return webClient.get()
                .uri("/users")
                .retrieve()
                .bodyToFlux(User.class)
                .retryWhen(Retry.backoff(5, Duration.ofSeconds(1)))
                .doOnError(error -> log.error("cannot get users", error))
                .flatMap(user -> Mono.fromRunnable(() -> emailUpdatesSender.sendEmail(user))
                        .doOnError(error -> log.error("cannot send email updates to users", error)))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

}
