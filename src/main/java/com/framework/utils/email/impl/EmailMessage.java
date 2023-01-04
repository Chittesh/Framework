package com.framework.utils.email.impl;

import java.util.Date;

import javax.mail.Message;

import com.framework.exception.AutomationException;

public class EmailMessage {
    private String subject = "";
    private Date date = null;
    private String from = "";
    private String text = "";

    public EmailMessage(Message message) {
        try {
            subject = message.getSubject();
            date = message.getSentDate();
            from = message.getFrom()[0].toString();
            text = message.getContent().toString();
        } catch (Exception e) {
            throw new AutomationException("Failed to create email message");
        }
    }

    public String getSubject() {
        return subject;
    }

    public Date getDate() {
        return date;
    }

    public String getFrom() {
        return from;
    }

    public String getText() {
        return text;
    }
}