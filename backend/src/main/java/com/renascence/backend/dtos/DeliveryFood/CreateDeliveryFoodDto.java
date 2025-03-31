package com.renascence.backend.dtos.DeliveryFood;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateDeliveryFoodDto {

    @NotNull
    @Positive
    private Long foodId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
