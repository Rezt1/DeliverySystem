package com.renascence.backend.dtos.DeliveryGuy;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDeliveryGuyDto {

    @NotNull
    private Long userId;

    private String iban;

    private Long restaurantId;
}
