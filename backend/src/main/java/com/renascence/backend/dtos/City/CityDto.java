package com.renascence.backend.dtos.City;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CityDto {

    private Long id;

    @NotBlank(message = "City name is required")
    @Size(min = 2, max = 100, message = "City name must be between 2-100 characters")
    private String name;

    public CityDto(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
