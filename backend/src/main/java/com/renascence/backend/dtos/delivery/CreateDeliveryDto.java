package com.renascence.backend.dtos.delivery;

import com.renascence.backend.dtos.deliveryFood.CreateDeliveryFoodDto;
import com.renascence.backend.enums.PaymentMethod;
import jakarta.validation.Valid;
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

    @Valid
    @NotEmpty(message = "At least 1 food is required")
    private List<CreateDeliveryFoodDto> foods;

    @NotNull(message = "Total price is required")
    @Positive(message = "Total price cannot be non positive")
    private Double totalPrice;

    @NotBlank(message = "Hour is required")
    @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "Hour should follow the pattern: hh:mm")
    private String hourToBeDelivered;
}
