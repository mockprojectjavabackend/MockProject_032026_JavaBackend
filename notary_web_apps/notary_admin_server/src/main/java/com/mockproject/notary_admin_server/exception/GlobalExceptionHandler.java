package com.mockproject.notary_admin_server.exception;

import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mockproject.notary_admin_server.dto.ApiErrorResponse;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiErrorResponse> handleAppException(
            AppException ex,
            HttpServletRequest request
    ) {
        log.warn("AppException [{}] {}", ex.getErrorCode().getCode(), ex.getMessage());

        Map<String, String> errors;

        if (ex.hasDetails() && ex.getDetails() != null && !ex.getDetails().isEmpty()) {
            errors = ex.getDetails().entrySet().stream()
                    .collect(java.util.stream.Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> String.valueOf(entry.getValue())
                    ));
        } else {
            errors = Map.of(
                    ex.getErrorCode().getCode(),
                    ex.getMessage()
            );
        }

        return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
                .body(ApiErrorResponse.of(
                        ex.getErrorCode().getHttpStatus().value(),
                        request.getRequestURI(),
                        errors
                ));
    }

    /**
     * Handle Bean Validation failures triggered by @Valid on @RequestBody parameters.
     * Collects all field-level constraint violations into a map of { fieldName -> message }
     * and returns a structured 400 Bad Request response.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        log.warn("Validation failed for request [{}]: {} error(s)",
                request.getRequestURI(), ex.getBindingResult().getErrorCount());

        //collect each field error into { fieldName -> defaultMessage }
        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value",
                        //if multiple violations exist for the same field, keep the first message
                        (existing, replacement) -> existing
                ));

        return ResponseEntity.badRequest()
                .body(ApiErrorResponse.of(400, request.getRequestURI(), errors));
    }

    private ResponseEntity<ApiErrorResponse> build(
            ErrorCode errorCode,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiErrorResponse.of(
                        errorCode.getHttpStatus().value(),
                        request.getRequestURI(),
                        Map.of(errorCode.getCode(), errorCode.getMessage())
                ));
    }
}