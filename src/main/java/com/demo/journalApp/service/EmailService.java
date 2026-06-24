package com.demo.journalApp.service;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String to, String subject, String body) {
        try {
            log.info("Inside sendMail.");
            sendMail(to, subject, body, null, null);
        } catch (Exception e) {
            log.error("Error in sendMail due to : {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void sendMail(String to, String subject, String body, String cc) {
        try {
            log.info("Inside sendMail..");
            sendMail(to, subject, body, cc, null);
        } catch (Exception e) {
            log.error("Error in sendMail due to :: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    public void sendMail(String to, String subject, String body, @Nullable String cc, @Nullable String bcc) {
        try {
            log.info("Inside sendMail...");
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);
            if (cc != null) mail.setCc(cc);
            if (bcc != null) mail.setBcc(bcc);
            javaMailSender.send(mail);
            log.info("Mail sent successfully.");
        } catch (Exception e) {
            log.error("Error in sendMail due to ::: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
