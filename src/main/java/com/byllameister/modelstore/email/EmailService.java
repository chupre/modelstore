package com.byllameister.modelstore.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${spring.mail.username}")
    private String serverEmail;

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(Email email) {
        sendSimpleEmail(email.getRecipientEmail(), email.getSubject(), email.getBody());
    }

    public void sendSimpleEmail(String recipientEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(serverEmail);
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
