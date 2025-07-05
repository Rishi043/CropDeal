package com.cropdeal.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    private String To;              // Dealer's email
    private String cropName;       // Crop name
    private double amount;         // Total amount
    private String transactionId;  // Stripe session ID or generated ID
    private String paymentDate;    // Date of payment
    private int quantity;          // Quantity in kg
    private String currency;       // Currency (e.g., USD, INR)

}
