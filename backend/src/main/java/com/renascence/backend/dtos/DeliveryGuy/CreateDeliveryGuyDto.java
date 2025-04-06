package com.renascence.backend.dtos.DeliveryGuy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateDeliveryGuyDto {

    @NotBlank(message = "IBAN is required")
    private String iban;

    @NotNull(message = "City id is required")
    @Positive
    private Long cityId;
}
