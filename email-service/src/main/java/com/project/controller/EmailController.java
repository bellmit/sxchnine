package com.project.controller;

import com.project.business.EmailContactSender;
import com.project.business.SubscriptionService;
import com.project.model.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailContactSender emailContactSender;
    private final SubscriptionService subscriptionService;

    @PostMapping("/contact")
    public void sendEmail(@RequestBody Contact contact) {
        emailContactSender.sendEmail(contact);
    }

    @PostMapping("/subscriptions")
    public void sendEmailToSubscribers() {
        subscriptionService.sendEmailToSubscribers();
    }
}
