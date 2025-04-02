package com.renascence.backend.dtos.Delivery;

import com.renascence.backend.dtos.DeliveryFood.CreateDeliveryFoodDto;
import com.renascence.backend.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class CreateDeliveryDto {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Delivery guy id is required")
    private Long deliveryGuyId;

    @NotNull(message = "Restaurant id is required")
    private Long restaurantId;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotEmpty(message = "At least 1 food is required")
    private List<CreateDeliveryFoodDto> foods;
}
