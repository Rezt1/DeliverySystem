package com.renascence.backend.dtos.DeliveryGuy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateDeliveryGuyDto {

    @NotNull(message = "Delivery guy id is required")
    @Positive
    private Long userId;

    @NotBlank(message = "IBAN is required")
    private String iban;

    @NotNull(message = "Restaurant id is required")
    @Positive
    private Long restaurantId;
}
