package com.renascence.backend.dtos.DeliveryFood;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateDeliveryFoodDto {

    @NotNull(message = "Food id is required")
    private Long foodId;

    @NotNull(message = "Food quantity is required")
    @Positive
    private Integer quantity;
}
