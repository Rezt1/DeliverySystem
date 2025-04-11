package com.renascence.backend.customAnnotations;

import com.renascence.backend.dtos.deliveryGuySalary.CreateDeliveryGuySalaryDto;
import com.renascence.backend.dtos.report.IncomeForPeriodOfTimeDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AfterDateValidator implements ConstraintValidator<AfterDate, Object> {
    @Override
    public void initialize(AfterDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        if (o instanceof CreateDeliveryGuySalaryDto) {

            CreateDeliveryGuySalaryDto dto = (CreateDeliveryGuySalaryDto) o;
            return !dto.getSalaryEndDate().isBefore(dto.getSalaryStartDate());

        } else if (o instanceof IncomeForPeriodOfTimeDto) {

            IncomeForPeriodOfTimeDto dto = (IncomeForPeriodOfTimeDto) o;
            return !dto.getEndDate().isBefore(dto.getStartDate());

        }

        return false;
    }
}
