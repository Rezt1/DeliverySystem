package com.renascence.backend.dtos.City;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class CreateCityDto {

    @NotBlank(message = "City name is required")
    @Size(min = 2, max = 100, message = "City name must be between 2-100 characters")
    private String name;

    public CreateCityDto(String name, Object o, Object o1) {
        this.name = name;
    }
}
