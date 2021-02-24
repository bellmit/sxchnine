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
        log.info("Send Confirmation Email to {} for Order ID: {}", order.getOrderKey().getUserEmail(), order.getOrderKey().getOrderId());
        Email emailFrom = new Email(from);
        Email emailTo = new Email(order.getOrderKey().getUserEmail());

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("commandId", order.getOrderKey().getOrderId());
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
