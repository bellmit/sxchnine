package com.project.controller;

import com.project.business.EmailSender;
import com.project.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class EmailController {

    private final EmailSender emailSender;

    public EmailController(@Qualifier("emailConfirmationSender")EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @PostMapping("/send")
    public void sendEmail(@RequestBody Order order){
        emailSender.sendEmail(order);
    }
}
