package com.project.business;

import com.project.model.User;
import com.sendgrid.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailForgotPasswordSender extends EmailSender<User>{

    @Value("${sendGrid.mail.templateForgotPasswordId}")
    private String templateForgotPasswordId;

    @Value("${sendGrid.mail.name}")
    private String senderName;

    public EmailForgotPasswordSender(SendGrid sendGrid) {
        super(sendGrid);
    }

    @Override
    public String getTemplateId() {
        return templateForgotPasswordId;
    }

    @Override
    public String type() {
        return "FORGOT_PASSWORD";
    }

    @Override
    public Mail mailBuilder(User user) {
        log.info("Send Forgot Password Email to user: {}", user.getEmail());
        Email emailFrom = new Email(from, senderName);
        Email emailTo = new Email(user.getEmail());

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("name", user.getLastName());
        personalization.addDynamicTemplateData("password", user.getPlainPassword());
        personalization.addTo(emailTo);

        Content content = new Content("text/html", "plain");
        Mail mail = new Mail();
        mail.setFrom(emailFrom);
        mail.setTemplateId(getTemplateId());
        mail.addPersonalization(personalization);
        mail.addContent(content);

        return mail;
    }
}
