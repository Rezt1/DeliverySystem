package com.renascence.backend.dtos.Food;

import com.renascence.backend.enums.FoodCategory;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateFoodDto {

    @NotBlank(message = "Food name is required")
    @Size(min = 2, max = 255, message = "Name must be 2-255 characters")
    private String name;

    @NotNull(message = "Food category is required")
    private FoodCategory foodCategory;

    @NotNull(message = "Price is required")
    @DecimalMin("0.01")
    private Double price;

    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;

    @NotNull(message = "Cuisine ID is required")
    @Positive
    private Long cuisineId;

    @NotNull(message = "Restaurant ID is required")
    @Positive
    private Long restaurantId;
}
