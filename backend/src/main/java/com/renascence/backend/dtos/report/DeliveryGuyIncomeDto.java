package com.renascence.backend.dtos.report;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DeliveryGuyIncomeDto {
    private long deliveryGuyId;
    private String deliveryGuyName;
    private String phoneNumber;
    private double amount;
    private LocalDate startDate;
    private LocalDate endDate;
}
