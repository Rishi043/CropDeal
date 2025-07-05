package com.cropdeal.notificationservice.controller;

import com.cropdeal.notificationservice.dto.EmailRequest;
import com.cropdeal.notificationservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/payment-success")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        emailService.sendPaymentEmail(request);
        return ResponseEntity.ok("Email sent successfully.");
    }
}
