package com.project.business;

import com.project.model.Order;
import com.sendgrid.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.io.IOException;

@RefreshScope
@Slf4j
public abstract class EmailSender {

    @Value("${sendGrid.mail.from}")
    private String from;

    private final SendGrid sendGrid;

    public EmailSender(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public abstract String getTemplateId();

    public void sendEmail(Order order) {
        log.info("sending email to {} with this templateId {}",
                order.getOrderPrimaryKey().getUserEmail(),
                getTemplateId());
        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mailBuilder(order).build());
            sendGrid.api(request);

        } catch (IOException e) {
            log.warn("Can't send email", e);
        }

    }

    private Mail mailBuilder(Order order) {
        Email emailFrom = new Email(from);
        Email emailTo = new Email(order.getOrderPrimaryKey().getUserEmail());

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("commandId", order.getOrderPrimaryKey().getOrderId());
        personalization.addDynamicTemplateData("total", order.getTotal());
        personalization.addDynamicTemplateData("currency", "$");
        personalization.addDynamicTemplateData("prix", order.getTotal());
        personalization.addDynamicTemplateData("taxe", "11");
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
