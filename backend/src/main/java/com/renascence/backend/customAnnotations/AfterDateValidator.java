package com.renascence.backend.customAnnotations;

import com.renascence.backend.dtos.deliveryGuySalary.CreateDeliveryGuySalaryDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AfterDateValidator implements ConstraintValidator<AfterDate, Object> {
    @Override
    public void initialize(AfterDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        CreateDeliveryGuySalaryDto dto = (CreateDeliveryGuySalaryDto) o;

        return dto.getSalaryEndDate().isAfter(dto.getSalaryStartDate());
    }
}
