package com.renascence.backend.dtos.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliverySystemStatistics {
    private long userCount;
    private long citiesCount;
    private long restaurantsCount;
    private long deliveryGuysCount;
}
