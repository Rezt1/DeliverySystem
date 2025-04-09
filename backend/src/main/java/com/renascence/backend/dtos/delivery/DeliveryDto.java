package com.renascence.backend.dtos.delivery;

import com.renascence.backend.dtos.deliveryFood.DeliveryFoodDto;
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
    private String username;
    private String address;
    private String userPhoneNumber;
    private LocalDateTime creationDate;
    private DeliveryStatus status;
    private PaymentMethod paymentMethod;
    private String deliveryGuyName;
    private String restaurantName;
    private List<DeliveryFoodDto> foods;

}
