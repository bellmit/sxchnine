package com.project.controller;

import com.project.business.EmailContactSender;
import com.project.model.Contact;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailContactSender emailContactSender;

    public EmailController(EmailContactSender emailContactSender) {
        this.emailContactSender = emailContactSender;
    }

    @PostMapping("/contact")
    public void sendEmail(@RequestBody Contact contact){
        emailContactSender.sendEmail(contact);
    }
}
