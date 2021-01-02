package com.project.business;

import com.project.model.Subscription;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class SubscriptionService {

    private final WebClient webClient;
    private final EmailSubscription emailSubscription;


    public void sendEmailToSubscribers(){
        getSubscribedUsers()
                .parallelStream()
                .forEach(emailSubscription::sendEmail);
    }

    private List<Subscription> getSubscribedUsers() {
        return Objects.requireNonNull(webClient.get()
                .uri("/subscriptions")
                .retrieve()
                .toEntityList(Subscription.class)
                .block())
                .getBody();
    }

}
