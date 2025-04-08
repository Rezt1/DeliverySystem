package com.renascence.backend.dtos.user;

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
