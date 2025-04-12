package com.renascence.backend.dtos.restaurant;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class EditRestaurantDto {
    @NotBlank(message = "Restaurant name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @Size(max = 50, message = "IBAN must be less than 50 characters")
    private String iban;

    @DecimalMin(value = "0.0", message = "Rating cannot be negative")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private Float rating;
}
