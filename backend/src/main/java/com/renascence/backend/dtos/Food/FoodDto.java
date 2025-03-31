package com.renascence.backend.dtos.Food;

import com.renascence.backend.enums.FoodCategory;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FoodDto {

    private long id;
    private String name;
    private FoodCategory foodCategory;
    private Double price;
    private String description;
    private Long cuisineId;
    private Long restaurantId;
}
