package com.project.business;

import com.project.model.Order;
import com.project.model.Subscription;
import com.sendgrid.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailSubscription extends EmailSender<Subscription> {

    @Value("${sendGrid.mail.templateSubscriptionId}")
    private String templateSubscriptionId;

    private SendGrid sendGrid;

    public EmailSubscription(SendGrid sendGrid) {
        super(sendGrid);
    }

    @Override
    public String getTemplateId() {
        return templateSubscriptionId;
    }

    @Override
    public String type() {
        return "SUBSCRIPTION";
    }

    @Override
    public Mail mailBuilder(Subscription subscription) {
        log.info("Send Subscription Email to {}", subscription.getEmail());
        Email emailFrom = new Email(from);
        Email emailTo = new Email(subscription.getEmail());

        Personalization personalization = new Personalization();
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
