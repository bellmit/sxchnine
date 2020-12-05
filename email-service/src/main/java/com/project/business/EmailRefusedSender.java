package com.project.business;

import com.project.utils.PaymentStatusCode;
import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.project.utils.PaymentStatusCode.REFUSED;

@Service
public class EmailRefusedSender extends EmailSender{

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
}
