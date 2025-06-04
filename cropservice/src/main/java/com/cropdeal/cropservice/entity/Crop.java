package com.cropdeal.cropservice.entity;

// Creating Entity class Crop to store crop details
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "crops")
@Data  // Lombok: Includes getters, setters, equals, hashCode, and toString
@NoArgsConstructor  // Lombok: Generates default constructor
@AllArgsConstructor // Lombok: Generates constructor with all fields
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Crop name is required")
    private String name;

    @NotBlank(message = "Crop type is required")
    private String type;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be at least 1")
    private Double price;

    // Getters and Setters
}
