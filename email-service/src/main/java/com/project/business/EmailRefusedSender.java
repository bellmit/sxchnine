package com.project.business;

import com.project.model.Order;
import com.project.utils.PaymentStatusCode;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.project.utils.PaymentStatusCode.REFUSED;

@Service
public class EmailRefusedSender extends EmailSender<Order>{

    @Value("${sendGrid.mail.templateRefusedId}")
    private String templateRefusedId;


    public EmailRefusedSender(SendGrid sendGrid) {
        super(sendGrid);
    }

    @Override
    public String getTemplateId() {
        return templateRefusedId;
    }

    @Override
    public String type() {
        return REFUSED.getValue();
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
