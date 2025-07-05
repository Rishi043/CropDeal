package com.cropdeal.notificationservice.service;


import com.cropdeal.notificationservice.dto.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPaymentEmail(EmailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getTo());
        message.setSubject("✅ Payment Successful - Crop Deal");

        String currencySymbol = switch (request.getCurrency()) {
            case "USD" -> "$";
            case "INR" -> "₹";
            default -> request.getCurrency() + " ";
        };

        String body = String.format("""
        🎉 Thank you for your purchase on Crop Deal!
        
        We are pleased to confirm that your payment of %s%.2f for the crop "%s" has been successfully processed.
        
        📦 Order Summary:
        • Crop Name: %s
        • Quantity: %d kg
        • Total Amount: %s%.2f
        • Payment Date: %s
        • Transaction ID: %s
        
        Thank you for choosing Crop Deal. We look forward to serving you again!
        
        Warm regards,
        🌾 Crop Deal Team
        """,
                currencySymbol, request.getAmount(),
                request.getCropName(),
                request.getCropName(),
                request.getQuantity(),
                currencySymbol, request.getAmount(),
                request.getPaymentDate(),
                request.getTransactionId()
        );
        message.setText(body);
        mailSender.send(message);
    }
}
