package com.renascence.backend.dtos.deliveryGuySalary;

import com.renascence.backend.customAnnotations.AfterDate;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AfterDate
public class CreateDeliveryGuySalaryDto {
    @NotNull(message = "Start date is required")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate salaryStartDate;

    @NotNull(message = "End date is required")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate salaryEndDate;
}
