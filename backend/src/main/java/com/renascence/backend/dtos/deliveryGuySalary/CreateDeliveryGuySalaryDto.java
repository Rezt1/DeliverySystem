package com.renascence.backend.dtos.deliveryGuySalary;

import com.renascence.backend.customAnnotations.AfterDate;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AfterDate
public class CreateDeliveryGuySalaryDto {
    @NotNull(message = "Start date is required")
    private LocalDate salaryStartDate;

    @NotNull(message = "Start date is required")
    private LocalDate salaryEndDate;
}
