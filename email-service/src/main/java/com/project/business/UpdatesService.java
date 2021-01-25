package com.project.business;

import com.project.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdatesService {

    private final WebClient webClient;
    private final EmailUpdatesSender emailUpdatesSender;


    public void sendEmailUpdatesToAllUsers(){
        getUsers()
                .parallelStream()
                .forEach(emailUpdatesSender::sendEmail);
    }

    private List<User> getUsers() {
        return webClient.get()
                .uri("/users")
                .retrieve()
                .bodyToFlux(User.class)
                .retryWhen(Retry.backoff(5, Duration.ofSeconds(1)))
                .onErrorResume(e -> Flux.empty())
                .collectList()
                .block();
    }

}
