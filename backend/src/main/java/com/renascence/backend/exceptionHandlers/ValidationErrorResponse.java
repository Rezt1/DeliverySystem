package com.renascence.backend.exceptionHandlers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ValidationErrorResponse(
        String message, Map<String, List<String>> errors, LocalDateTime timestamp
) {}
