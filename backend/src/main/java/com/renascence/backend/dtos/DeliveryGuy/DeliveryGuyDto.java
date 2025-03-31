package com.renascence.backend.dtos.DeliveryGuy;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryGuyDto {

    private Long id;
    private String name;
    private String phone;
    private Integer activeDeliveriesCount; // Critical for assignment logic
}
