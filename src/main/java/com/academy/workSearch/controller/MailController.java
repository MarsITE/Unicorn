package com.academy.workSearch.controller;

import com.academy.workSearch.model.Mail;
import com.academy.workSearch.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.ValidationException;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class MailController {
EmailService emailService;

    @PostMapping("/toadmin")
    @ResponseStatus(HttpStatus.OK)
    public void sendingMail(@RequestBody Mail mail, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new ValidationException("Message is not valid");
        }
        emailService.sendingMessage(mail);
    }

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessage(@RequestBody Mail mail, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new ValidationException("Message is not valid");
        }
        emailService.sendingMessage(mail);
    }

    @PostMapping("/attachment")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessageWhitAttachment(@RequestBody Mail mail, @RequestParam String path, BindingResult bindingResult) throws MessagingException {
        if(bindingResult.hasErrors()){
            throw new ValidationException("Message is not valid");
        }
        emailService.sendMessageWithAttachment(mail, path);
    }

}
