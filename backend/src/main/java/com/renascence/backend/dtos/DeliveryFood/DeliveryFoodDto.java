package com.renascence.backend.dtos.DeliveryFood;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeliveryFoodDto {

    private Long deliveryFoodId;
    private String foodName;
    private Integer quantity;

}
