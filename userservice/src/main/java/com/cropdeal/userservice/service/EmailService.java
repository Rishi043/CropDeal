package com.cropdeal.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetEmail(String to, String token) {
        String subject = "🔐 Crop Deal Password Reset Request";
        String resetUrl = "http://localhost:3000/reset-password/" + token;

        String body = String.format("""
        We received a request to reset your password for your Crop Deal account.

        🚨 For your security, this link will expire in 15 minutes.
        Click below to reset your password:
        👉 %s

        If you didn’t request this, just ignore this message — your account is safe.

        Cheers,
        🌾 Crop Deal Team
        """, resetUrl);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);

        mailSender.send(mailMessage);
    }
}
