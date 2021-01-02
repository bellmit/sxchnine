package com.project.business;

import com.project.model.Order;
import com.project.utils.PaymentStatusCode;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.project.utils.PaymentStatusCode.CONFIRMED;

@Service
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
        Email emailFrom = new Email(from);
        Email emailTo = new Email(order.getOrderKey().getUserEmail());

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("commandId", order.getOrderKey().getOrderId());
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
