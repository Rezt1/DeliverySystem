package com.renascence.backend.dtos.authorization;

public record AuthResponseDto
        (String accessToken,
         String email,
         String username,
         String phoneNumber) { }
