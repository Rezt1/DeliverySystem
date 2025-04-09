package com.renascence.backend.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDto {
    @NotBlank(message = "Name is required!")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters!")
    private String name;

    @NotBlank(message = "Email must contain letters!")
    @Email(message = "Invalid email!")
    private String email;

    private String password;

    @NotBlank(message = "Phone is required")
    @Size(min = 7, max = 255, message = "Phone number should be at least 7 digits long!")
    private String phoneNumber;

    private Long locationId;
}
