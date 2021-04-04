package com.project.business;

import com.project.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {

    private final EmailForgotPasswordSender emailForgotPasswordSender;
    private final EmailWelcomeSender emailWelcomeSender;


    @KafkaListener(groupId = "${kafka.user.groupId}",
            topics = "${kafka.user.topic}",
            containerFactory = "usersKafkaListenerContainerFactory")
    public void consumeUser(User user, Acknowledgment acknowledgment) {
        log.info("User Received: {}", user.getEmail());
        if (user.isForgotPassword()) {
            emailForgotPasswordSender.sendEmail(user);
        } else {
            emailWelcomeSender.sendEmail(user);
        }
        acknowledgment.acknowledge();
    }
}
