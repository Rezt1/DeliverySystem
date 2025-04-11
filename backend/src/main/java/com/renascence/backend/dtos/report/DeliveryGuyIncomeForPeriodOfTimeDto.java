package com.renascence.backend.dtos.report;

import com.renascence.backend.customAnnotations.AfterDate;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AfterDate
public class DeliveryGuyIncomeForPeriodOfTimeDto {
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;
}
