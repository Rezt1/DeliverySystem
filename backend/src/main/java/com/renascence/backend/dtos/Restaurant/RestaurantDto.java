package com.renascence.backend.dtos.Restaurant;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantDto {

    private Long id;

    @NotBlank(message = "Restaurant name is required")
    @Size(min = 2, max = 255, message = "City name must be between 2-255 characters")
    private String name;

    @DecimalMin(value = "0.0", message = "Rating cannot be negative")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private Float rating;

    @NotNull(message = "City ID is required")
    @Positive(message = "City ID must be a positive number")
    private Long cityId;

    // ADDITIONAL INFO MAYBE WE DON'T NEED! FEEL FREE TO DELETE IT
//    @NotNull(message = "Owner ID is required")
//    @Positive(message = "Owner ID must be a positive number")
//    private long ownerId;
//
//    @Size(max = 50, message = "IBAN must be less than 50 characters")
//    private String iban;
//
//    @DecimalMin(value = "0.0", message = "Delivery guy salary cannot be negative")
//    private double deliveryGuySalary;
}
