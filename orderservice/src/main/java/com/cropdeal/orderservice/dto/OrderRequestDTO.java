// OrderRequestDTO.java
package com.cropdeal.orderservice.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderRequestDTO {
    @NotNull(message = "Crop ID is required")
    @Min(value = 1, message = "Crop ID must be positive")
    private Long cropId;

    @NotBlank(message = "Dealer email is required")
    @Email(message = "Invalid email format")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Email format must be valid"
    )
    private String dealerEmail;
}