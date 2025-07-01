package com.cropdeal.paymentservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Lombok: Includes getters, setters, equals, hashCode, and toString
@NoArgsConstructor  // Lombok: Generates default constructor
@AllArgsConstructor // Lombok: Generates constructor with all fields
public class Crop {
    private Long id;
    private String farmerMail;  // New Field
    private String name;
    private String type;
    private Double pricePerKg;
    private Integer totalQuantity;  // New Field
}
