package com.project.business;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
}
