package com.renascence.backend.dtos.Delivery;

import com.renascence.backend.dtos.DeliveryFood.CreateDeliveryFoodDto;
import com.renascence.backend.enums.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CreateDeliveryDto {

    @NotBlank(message = "Address is required")
    @Size(min = 1, max = 200, message = "Address shouldn't be more than 200 characters long")
    private String address;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotEmpty(message = "At least 1 food is required")
    private List<CreateDeliveryFoodDto> foods;
}
