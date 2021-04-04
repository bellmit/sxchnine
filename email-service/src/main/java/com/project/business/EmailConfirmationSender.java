package com.project.business;

import com.project.model.Order;
import com.sendgrid.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.project.utils.PaymentStatusCode.CONFIRMED;

@Service
@Slf4j
public class EmailConfirmationSender extends EmailSender<Order> {

    @Value("${sendGrid.mail.templateConfirmationId}")
    private String templateConfirmationId;

    @Value("${sendGrid.mail.name}")
    private String senderName;

    public EmailConfirmationSender(SendGrid sendGrid) {
        super(sendGrid);
    }

    @Override
    public String getTemplateId() {
        return templateConfirmationId;
    }

    @Override
    public String type() {
        return CONFIRMED.getValue();
    }

    @Override
    public Mail mailBuilder(Order order) {
        log.info("Send Confirmation Email to {} for Order ID: {}", order.getUserEmail(), order.getOrderId());
        Email emailFrom = new Email(from, senderName);
        Email emailTo = new Email(order.getUserEmail());

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("commandId", order.getOrderId());
        personalization.addDynamicTemplateData("total", order.getTotal());
        personalization.addDynamicTemplateData("currency", "$");
        personalization.addDynamicTemplateData("prix", order.getTotal());
        personalization.addDynamicTemplateData("taxe", "7");
        personalization.addTo(emailTo);
        personalization.addBcc(emailFrom);

        Content content = new Content("text/html", "plain");
        Mail mail = new Mail();
        mail.setFrom(emailFrom);
        mail.setTemplateId(getTemplateId());
        mail.addPersonalization(personalization);
        mail.addContent(content);

        return mail;
    }


}
