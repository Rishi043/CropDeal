package com.cropdeal.paymentservice.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data  // Lombok: Includes getters, setters, equals, hashCode, and toString
@NoArgsConstructor  // Lombok: Generates default constructor
@AllArgsConstructor // Lombok: Generates constructor with all fields
public class Order {
    private Long orderId;
    private Long cropId;
    private String dealerEmail;
    private LocalDate orderDate;
    private String status;

}
