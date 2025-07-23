//package com.cropdeal.paymentservice.dto;
package com.cropdeal.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequest {

    @NotNull(message = "Order ID is required")
    private Long orderId;
}
























//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "payments")
//@Entity
//public class PurchaseRequest {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID
//    private Long id; // Primary key
//    private Long orderId;
//    private String crop;
//    private Integer quantity;
//    private String currency;
//    private Long amount;
//}

