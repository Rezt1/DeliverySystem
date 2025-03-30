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
    private String name;
    private Float rating;
    private Long cityId;
}
