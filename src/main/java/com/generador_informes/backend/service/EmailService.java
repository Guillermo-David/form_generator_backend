package com.generador_informes.backend.service;

import java.io.File;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.generador_informes.backend.Names;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendReport(String to, String subject, String text, File pdf) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        helper.addAttachment(pdf.getName(), pdf);
        mailSender.send(message);
        System.out.println(Names.EMAIL_ENVIADO.get() + to);
    }
    
    public void sendEmailWithAttachments(String to, String subject, String text, String... attachments) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            for (String path : attachments) {
                FileSystemResource file = new FileSystemResource(path);
                helper.addAttachment(file.getFilename(), file);
            }

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

