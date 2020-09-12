package com.project.business;

import com.project.model.Contact;
import com.sendgrid.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RefreshScope
@Slf4j
public class EmailContactSender {

    @Value("${sendGrid.mail.templateContact}")
    private String templateContactId;

    @Value("${sendGrid.mail.from}")
    private String from;

    private final SendGrid sendGrid;

    public EmailContactSender(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public void sendEmail(Contact contact) {
        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mailBuilder(contact).build());
            sendGrid.api(request);

        } catch (IOException e) {
            log.warn("Can't send email", e);
        }

    }

    private Mail mailBuilder(Contact contact) {
        Email emailFrom = new Email(from);
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
