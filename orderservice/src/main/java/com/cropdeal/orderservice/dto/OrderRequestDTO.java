package com.cropdeal.orderservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class OrderRequestDTO {

    @NotNull(message = "Crop ID is required")
    @Min(value = 1, message = "Crop ID must be positive")
    private Long cropId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Double quantity;

    // ❌ Removed dealerEmail (we'll fetch from JWT in controller)
}


// OrderRequestDTO.java
//package com.cropdeal.orderservice.dto;
//import jakarta.validation.constraints.*;
//        import lombok.Data;
//
//@Data
//public class OrderRequestDTO {
//    @NotBlank(message = "Crop Name is Required")
//    private String cropName;
//
//    @NotBlank(message = "Dealer email is required")
//    @Email(message = "Invalid email format")
//    @Pattern(
//            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
//            message = "Email format must be valid"
//    )
//    private Double quantity;
//}
//
//
//