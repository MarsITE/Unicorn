package com.academy.workSearch.service.implementation;

import com.academy.workSearch.model.Mail;
import com.academy.workSearch.service.EmailService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {
    JavaMailSenderImpl emailSender;
    Environment environment;

    /**
     *
     * @param mail entity with email, subject and message for recipient
     */
    @Override
    public void sendingMessage(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Objects.requireNonNull(environment.getProperty("spring.mail.username")));
        message.setTo(mail.getEmail());
        message.setSubject(mail.getSubject());
        message.setText(mail.getMessage());
        emailSender.send(message);
    }

    /**
     *
     * @param mail entity with email, subject and message for recipient
     * @param pathToAttachment attachment
     * @throws MessagingException
     */
    @Override
    public void sendMessageWithAttachment(Mail mail, String pathToAttachment) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(Objects.requireNonNull(environment.getProperty("spring.mail.username")));
        helper.setTo(mail.getEmail());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getMessage());

        FileSystemResource file
                = new FileSystemResource(new File(pathToAttachment));
        helper.addAttachment("Invoice.txt", file);

        emailSender.send(message);
    }

    /**
     *
     * @param mail entity with email, subject and message for recipient
     */
    @Override
    public void sendHtmlMessage(Mail mail) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(mail.getEmail());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getMessage(), true);
            emailSender.send(message);
        } catch (MessagingException e) {
//            throw new MessagingException("Incorect user data");
        }

    }
}
