package com.cropdeal.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private Long cropId;
    private String dealerEmail;
    private Double quantity;
    private LocalDate orderDate;
    private String status ="Pending";
}
