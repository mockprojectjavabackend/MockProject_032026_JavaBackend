package com.mockproject.notary_admin_server.exception;

import com.mockproject.notary_admin_server.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Core - Custom Application Exception Handler
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

    // Validation Exception Handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
                                                                      HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            field = field.replaceAll("\\[.*?\\]","");
            errors.put(field, error.getDefaultMessage());
        });

        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .errors(errors)
                .timestamp(Instant.now().toString())
                .build()
                ;

        return ResponseEntity.badRequest().body(response);
    }

    /* ================= UTIL ================= */

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