package com.project.business;

import com.project.model.Order;
import com.sendgrid.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.project.utils.PaymentStatusCode.REFUSED;
import static com.project.utils.PaymentStatusCode.WAITING;

@Service
@Slf4j
public class EmailPendingSender extends EmailSender<Order> {

    @Value("${sendGrid.mail.templatePendingId}")
    private String templatePendingId;

    @Value("${sendGrid.mail.name}")
    private String senderName;

    public EmailPendingSender(SendGrid sendGrid) {
        super(sendGrid);
    }

    @Override
    public String getTemplateId() {
        return templatePendingId;
    }

    @Override
    public String type() {
        return WAITING.getValue();
    }

    @Override
    public Mail mailBuilder(Order order) {
        log.info("Send Pending Email to {} for Order ID: {}", order.getUserEmail(), order.getOrderId());
        Email emailFrom = new Email(from, senderName);
        Email emailTo = new Email(order.getUserEmail());

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("commandId", order.getOrderId());
        personalization.addDynamicTemplateData("total", order.getTotal());
        personalization.addDynamicTemplateData("currency", "$");
        personalization.addDynamicTemplateData("prix", order.getTotal());
        personalization.addDynamicTemplateData("taxe", "11");
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
