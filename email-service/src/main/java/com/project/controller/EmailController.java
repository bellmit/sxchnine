package com.project.controller;

import com.project.business.EmailContactSender;
import com.project.business.EmailWelcomeSender;
import com.project.business.SubscriptionService;
import com.project.business.UpdatesService;
import com.project.model.Contact;
import com.project.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailContactSender emailContactSender;
    private final SubscriptionService subscriptionService;
    private final UpdatesService updatesService;
    private final EmailWelcomeSender emailWelcomeSender;

    @PostMapping("/contact")
    public Mono<Void> sendEmail(@RequestBody Contact contact) {
        return Mono.fromRunnable(() -> emailContactSender.sendEmail(contact))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @PostMapping("/subscriptions")
    public Mono<Void> sendEmailToSubscribers() {
        return subscriptionService.sendEmailToSubscribers();
    }

    @PostMapping("/updateUsers")
    public Mono<Void> sendUpdateToUsers() {
        return updatesService.sendEmailUpdatesToAllUsers();
    }


    @PostMapping("/welcomeUser")
    public Mono<Void> sendUpdateToUsers(@RequestBody User user) {
        return Mono.fromRunnable(() -> emailWelcomeSender.sendEmail(user));
    }



}
