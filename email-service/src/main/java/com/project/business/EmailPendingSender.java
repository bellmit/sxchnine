package com.project.business;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.project.utils.PaymentStatusCode.REFUSED;
import static com.project.utils.PaymentStatusCode.WAITING;

@Service
public class EmailPendingSender extends EmailSender {

    @Value("${sendGrid.mail.templatePendingId}")
    private String templatePendingId;

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
}
