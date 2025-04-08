package com.renascence.backend.dtos.City;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class CreateCityDto {

    @NotBlank(message = "City name is required")
    @Size(min = 2, max = 100, message = "City name must be between 2-100 characters")
    private String name;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary should be positive")
    private Double salary;
}
