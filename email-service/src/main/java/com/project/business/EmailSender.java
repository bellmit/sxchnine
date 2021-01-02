package com.project.business;

import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.io.IOException;

@RefreshScope
@Slf4j
public abstract class EmailSender<T> {

    @Value("${sendGrid.mail.from}")
    protected String from;

    protected final SendGrid sendGrid;

    public EmailSender(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public abstract String getTemplateId();

    public abstract String type();

    public abstract Mail mailBuilder(T object);

    public void sendEmail(T object) {
        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mailBuilder(object).build());
            sendGrid.api(request);

        } catch (IOException e) {
            log.warn("Can't send email", e);
        }
    }

}
