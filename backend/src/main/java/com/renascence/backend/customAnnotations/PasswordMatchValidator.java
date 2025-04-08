package com.renascence.backend.customAnnotations;

import com.renascence.backend.dtos.authorization.RegisterDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatcher, Object> {
    @Override
    public void initialize(PasswordMatcher constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        RegisterDto registerDto = (RegisterDto) o;
        return registerDto.getPassword().equals(registerDto.getRepeatPassword());
    }
}
