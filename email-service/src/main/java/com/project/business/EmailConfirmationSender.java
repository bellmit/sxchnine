package com.project.business;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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


}
