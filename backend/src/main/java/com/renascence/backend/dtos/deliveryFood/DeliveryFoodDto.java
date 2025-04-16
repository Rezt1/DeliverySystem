package com.renascence.backend.dtos.deliveryFood;
import lombok.*;

@Data
@Getter
@Setter
public class DeliveryFoodDto {

    private Long deliveryFoodId;
    private String foodName;
    private Integer quantity;
}
