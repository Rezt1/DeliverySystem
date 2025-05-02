package com.renascence.backend.dtos.authorization;

import com.renascence.backend.customAnnotations.PasswordMatcher;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@PasswordMatcher
@NoArgsConstructor
public class RegisterDto {

    @NotBlank(message = "Name is required!")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters!")
    private String name;

    @NotBlank(message = "Email must contain letters!")
    @Email(message = "Invalid email!")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$",
            message = "Password should contain at least: 1 lowercase letter, 1 uppercase letter, 1 digit, no spaces and tabs and should be at least 6 characters long!")
    private String password;

    private String repeatPassword;

    @NotBlank(message = "Phone is required")
    @Size(min = 7, max = 255, message = "Phone number should be at least 7 digits long!")
    private String phoneNumber;

    private Long locationId;

    public RegisterDto(String name, String email, String password, String repeatPassword, String phoneNumber, Long locationId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.phoneNumber = phoneNumber;
        this.locationId = locationId;
    }
}
