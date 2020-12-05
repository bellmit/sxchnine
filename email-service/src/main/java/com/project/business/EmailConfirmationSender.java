package com.project.business;

import com.project.utils.PaymentStatusCode;
import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.project.utils.PaymentStatusCode.CONFIRMED;

@Service
public class EmailConfirmationSender extends EmailSender {

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


}
