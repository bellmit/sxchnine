package com.project.business;

import com.project.model.Subscription;
import com.project.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriberConsumer {

    private final EmailWelcomeSender emailWelcomeSender;


    @KafkaListener(groupId = "${kafka.subscriber.groupId}",
            topics = "${kafka.subscriber.topic}",
            containerFactory = "subscribersKafkaListenerContainerFactory")
    public void consumeOrder(Subscription subscription, Acknowledgment acknowledgment) {
        log.info("Subscriber Received: {}", subscription.getEmail());
        User user = new User();
        user.setEmail(subscription.getEmail());
        emailWelcomeSender.sendEmail(user);
        acknowledgment.acknowledge();
    }
}
