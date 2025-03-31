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

    @NotNull
    private Long userId;

    @NotNull
    private Long deliveryGuyId;

    @NotBlank
    private String address;

    @NotNull
    private PaymentMethod paymentMethod;

    @NotEmpty
    private List<CreateDeliveryFoodDto> foods;
}
