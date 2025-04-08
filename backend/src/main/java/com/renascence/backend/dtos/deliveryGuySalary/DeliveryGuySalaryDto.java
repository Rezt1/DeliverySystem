package com.renascence.backend.dtos.deliveryGuySalary;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DeliveryGuySalaryDto {
    private String deliveryGuyName;
    private double amount;
    private LocalDate startDate;
    private LocalDate endDate;
    private double bonusAmount;
}
