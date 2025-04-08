package com.renascence.backend.dtos.deliveryGuy;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeliveryGuyDto {

    private Long userId; // Delivery guy is user
    // id is foreign key to user entity so we will get name and phone number through id
    private String deliveryGuyName;
    private String deliveryGuyPhoneNumber;
    private String iban;
    private String workCity;
}
