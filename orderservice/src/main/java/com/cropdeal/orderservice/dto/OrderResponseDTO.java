package com.cropdeal.orderservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderResponseDTO {
    private Long orderId;
    private String dealerEmail;
    private String cropName;
    private Double quantity;
    private LocalDate orderDate;
    private String status;
}
