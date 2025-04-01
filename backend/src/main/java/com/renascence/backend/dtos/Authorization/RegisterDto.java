package com.renascence.backend.dtos.Authorization;

import com.renascence.backend.customAnnotations.PasswordMatcher;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@PasswordMatcher
public class RegisterDto {

    @Length(min = 2, max = 255, message = "Name must be between 2 and 255 characters!")
    private String name;

    @NotBlank(message = "Email must contain letters!")
    @Email(message = "Invalid email!")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$",
            message = "Password should contain at least: 1 lowercase letter, 1 uppercase letter, 1 digit, no spaces and tabs and should be at least 6 characters long!")
    private String password;

    private String repeatPassword;

    @Length(min = 7, max = 255, message = "Phone number should be at least 7 digits long!")
    private String phoneNumber;

    @Positive
    private Long locationId;
}
