package com.cropdeal.paymentservice.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
@Entity
public class PurchaseRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID
    private Long id; // Primary key
    private Long orderId;
    private String crop;
    private Integer quantity;
    private String currency;
    private Long amount;
}