package com.project.business;

import com.project.model.Contact;
import com.project.utils.PaymentStatusCode;
import com.sendgrid.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.project.utils.PaymentStatusCode.CONTACT;

@Service
@RefreshScope
@Slf4j
public class EmailContactSender extends EmailSender<Contact>{

    @Value("${sendGrid.mail.templateContact}")
    private String templateContactId;

    @Value("${sendGrid.mail.name}")
    private String senderName;

    public EmailContactSender(SendGrid sendGrid) {
        super(sendGrid);
    }

    @Override
    public String getTemplateId() {
        return templateContactId;
    }

    @Override
    public String type() {
        return CONTACT.getValue();
    }

    @Override
    public Mail mailBuilder(Contact contact) {
        log.info("Send Contact Email to Naybxrz Support: {}", from);
        Email emailFrom = new Email(from, senderName);
        Email emailTo = new Email(from);

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("fullName", contact.getFullName());
        personalization.addDynamicTemplateData("email", contact.getEmail());
        personalization.addDynamicTemplateData("phone", contact.getPhoneNumber());
        personalization.addDynamicTemplateData("message", contact.getMessage());
        personalization.addTo(emailTo);

        Content content = new Content("text/html", "plain");
        Mail mail = new Mail();
        mail.setFrom(emailFrom);
        mail.setTemplateId(templateContactId);
        mail.addPersonalization(personalization);
        mail.addContent(content);

        return mail;
    }
}
