package com.project.business;

import com.project.model.Order;
import com.sendgrid.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import static com.project.utils.PaymentStatusCode.SHIPPED;

@Service
@RefreshScope
@Slf4j
public class EmailShippedSender extends EmailSender<Order> {

    @Value("${sendGrid.mail.templateShippedId}")
    private String templateShippedId;

    @Value("${sendGrid.mail.name}")
    private String senderName;

    public EmailShippedSender(SendGrid sendGrid) {
        super(sendGrid);
    }

    @Override
    public String getTemplateId() {
        return templateShippedId;
    }

    @Override
    public String type() {
        return SHIPPED.getValue();

    }

    @Override
    public Mail mailBuilder(Order order) {
        log.info("Send Shipped Email to {} for Order ID: {}", order.getUserEmail(), order.getOrderId());
        Email emailFrom = new Email(from, senderName);
        Email emailTo = new Email(order.getUserEmail());

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("name", order.getUserAddress().getFirstName());
        personalization.addDynamicTemplateData("orderID", order.getOrderId());
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
