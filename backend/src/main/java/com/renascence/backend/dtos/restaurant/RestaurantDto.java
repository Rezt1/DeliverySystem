package com.renascence.backend.dtos.restaurant;

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
    private String cityName;

}
