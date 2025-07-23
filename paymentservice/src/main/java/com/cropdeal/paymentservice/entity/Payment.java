package com.cropdeal.paymentservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment")  // Optional: matches the table name in DB
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long paymentId;
        private Long orderId;
        private String dealerEmail;
        private String cropName;
        private int quantity;
        private Double amount;
        private LocalDateTime paymentDate;
    }