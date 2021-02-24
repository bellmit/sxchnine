package com.project.business;

import com.project.model.User;
import com.sendgrid.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailWelcomeSender extends EmailSender<User> {

    @Value("${sendGrid.mail.templateWelcomeUserId}")
    private String templateWelcomeUserId;

    public EmailWelcomeSender(SendGrid sendGrid) {
        super(sendGrid);
    }

    @Override
    public String getTemplateId() {
        return templateWelcomeUserId;
    }

    @Override
    public String type() {
        return "WELCOME_USER";
    }

    @Override
    public Mail mailBuilder(User user) {
        log.info("Send Welcome Email to {}", user.getEmail());
        Email emailFrom = new Email(from);
        Email emailTo = new Email(user.getEmail());

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("name", user.getLastName());
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
