package com.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired; 

@Service
@RequiredArgsConstructor
public class EmailService {

	@Autowired
    private  JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); 
            helper.setFrom("guedoiirkhalil@gmail.com"); 

            mailSender.send(message);
            System.out.println("✅ Email envoyé à " + to);
        } catch (MessagingException e) {
            System.err.println("❌ Erreur lors de la création du message : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erreur d’envoi d’email : " + e.getMessage());
        }
    }
}
