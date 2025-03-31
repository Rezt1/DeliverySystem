package com.renascence.backend.exceptionHandlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle validation errors (e.g., @NotBlank, @NotNull)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        // Extract the first error message (e.g., "name" field error)
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> {
                    String field = fieldError.getField();
                    String message = fieldError.getDefaultMessage();
                    return String.format("%s: %s", field, message);
                })
                .orElse("Validation failed");


        ErrorResponse response = new ErrorResponse(
                errorMessage,
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(response);
    }

    // Handle other exceptions (optional)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralExceptions(Exception ex) {
        ErrorResponse response = new ErrorResponse(
                "An unexpected error occurred: " + ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.internalServerError().body(response);
    }
}
