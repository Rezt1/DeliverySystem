package com.renascence.backend.dtos.Restaurant;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateRestaurantDto {

    @NotBlank(message = "Restaurant name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotNull(message = "City ID is required")
    @Positive(message = "City ID must be a positive number")
    private Long cityId;

    @NotNull(message = "Owner ID is required")
    @Positive(message = "Owner ID must be a positive number")
    private Long ownerId;

    @Size(max = 50, message = "IBAN must be less than 50 characters")
    private String iban;

    @DecimalMin(value = "0.0", message = "Rating cannot be negative")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private Float rating;
}