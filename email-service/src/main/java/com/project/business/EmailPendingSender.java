package com.project.business;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
}
