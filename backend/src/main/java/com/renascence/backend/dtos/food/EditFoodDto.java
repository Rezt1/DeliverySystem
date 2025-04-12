package com.renascence.backend.dtos.food;

import com.renascence.backend.enums.FoodCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class EditFoodDto {
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
}
