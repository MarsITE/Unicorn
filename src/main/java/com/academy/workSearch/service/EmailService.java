package com.academy.workSearch.service;

import com.academy.workSearch.model.Mail;

import javax.mail.MessagingException;

public interface EmailService {
    void sendingMessage(Mail mail);

    void sendMessageWithAttachment(Mail mail, String pathToAttachment) throws MessagingException;

    void sendHtmlMessage(Mail mail);
}
