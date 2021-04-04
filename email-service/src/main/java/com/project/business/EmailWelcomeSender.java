package com.project.business;

import com.project.model.User;
import com.sendgrid.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class EmailWelcomeSender extends EmailSender<User> {

    @Value("${sendGrid.mail.templateWelcomeUserId}")
    private String templateWelcomeUserId;

    @Value("${sendGrid.mail.name}")
    private String senderName;

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
        Email emailFrom = new Email(from, senderName);
        Email emailTo = new Email(user.getEmail());

        Personalization personalization = new Personalization();
        if (!StringUtils.isEmpty(user.getLastName())) {
            personalization.addDynamicTemplateData("name", user.getLastName());
        }
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
