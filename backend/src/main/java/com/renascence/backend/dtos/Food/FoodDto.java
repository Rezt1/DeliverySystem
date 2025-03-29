package com.renascence.backend.dtos.Food;

import com.renascence.backend.enums.FoodCategory;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FoodDto {

    private long id;

    @NotBlank(message="Food name is required")
    @Size(min=2, max=255, message="Food name must be between 2-255 characters")
    private String name;

    @NotNull(message = "Food category is required")
    private FoodCategory foodCategory;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be ≥ 0.01")
    private Double price;

    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;

    @NotNull(message = "Cuisine ID is required")
    @Positive(message = "Cuisine ID must be positive")
    private Long cuisineId;

    @NotNull(message = "Restaurant ID is required")
    @Positive(message = "Restaurant ID must be positive")
    private Long restaurantId;
}
