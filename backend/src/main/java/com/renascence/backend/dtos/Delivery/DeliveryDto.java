package com.renascence.backend.dtos.Delivery;

import com.renascence.backend.dtos.DeliveryFood.DeliveryFoodDto;
import com.renascence.backend.enums.DeliveryStatus;
import com.renascence.backend.enums.PaymentMethod;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeliveryDto {

    private Long deliveryId;
    private Long userId;
    private String address;
    private LocalDateTime date;
    private DeliveryStatus status;
    private PaymentMethod paymentMethod;
    private Long deliveryGuyId;
    private Long restaurantId;
    private List<DeliveryFoodDto> foods;
}
