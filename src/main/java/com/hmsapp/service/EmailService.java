package com.hmsapp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailId;

    public void sendEmail(String to,String subject,String body){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(emailId);

        mailSender.send(message);
    }

    public void sendEmailWithAttachment(String to, String subject, String body, String pdfFilePath) throws MessagingException {
        // Create a MimeMessage
        MimeMessage message = mailSender.createMimeMessage();

        // Use MimeMessageHelper to configure the email with an attachment
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);
        helper.setFrom(emailId);

        // Attach the PDF file
        File pdfFile = new File(pdfFilePath);
        if (pdfFile.exists()) {
            helper.addAttachment(pdfFile.getName(), pdfFile);
        } else {
            throw new MessagingException("PDF file not found at: " + pdfFilePath);
        }

        // Send the email
        mailSender.send(message);
    }
}
