package com.renascence.backend.dtos.DeliveryFood;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryFoodDto {

    private Long id;
    private Integer quantity;
    private String foodName;  // Added for client convenience
    private Double foodPrice; // Snapshot of price at order time
}
