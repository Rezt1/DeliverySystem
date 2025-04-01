package com.renascence.backend.exceptionHandlers;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(
        String message, Map<String, String> errors, LocalDateTime timestamp
) {}
