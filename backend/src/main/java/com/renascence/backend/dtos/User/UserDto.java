package com.renascence.backend.dtos.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private String email;
    private String name;
    private String phoneNumber;
    private String cityName;

}
