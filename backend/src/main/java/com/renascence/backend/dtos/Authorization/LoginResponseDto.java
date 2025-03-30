package com.renascence.backend.dtos.Authorization;

public record LoginResponseDto(String accessToken, String refreshToken) {
}
