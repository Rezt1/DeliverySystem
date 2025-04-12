package com.renascence.backend.dtos.city;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditCityDto {
    private Long id;
    private String name;
    private Double salary;
}
