package com.cropdeal.orderservice.feign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor // Lombok: Generates constructor with all fields
public class Crop {

    private Long id;
    private String farmerMail;  // New Field
    private String name;
    private String type;
    private Double pricePerKg;
    private Integer totalQuantity;  // New Field


}

