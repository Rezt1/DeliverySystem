package com.renascence.backend.dtos.Authorization;

public record AuthResponseDto
        (String accessToken,
         String email,
         String username,
         String phoneNumber) { }
