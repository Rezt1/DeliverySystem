package com.renascence.backend.dtos.Cuisine;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCuisineDto {

    @NotBlank(message = "Cuisine name is required")
    @Size(min = 2, max = 100, message = "Cuisine name must be between 2-100 characters")
    private String name;
}
