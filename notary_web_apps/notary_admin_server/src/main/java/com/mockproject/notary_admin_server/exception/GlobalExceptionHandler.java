package com.mockproject.notary_admin_server.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mockproject.notary_admin_server.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiErrorResponse> handleAppException(
            AppException ex,
            HttpServletRequest request
    ) {
        log.warn("AppException [{}] {}", ex.getErrorCode().getCode(), ex.getMessage());

        Map<String, String> errors = new LinkedHashMap<>();

        if (ex.hasDetails() && ex.getDetails() != null && !ex.getDetails().isEmpty()) {
            for (Map.Entry<String, Object> entry : ex.getDetails().entrySet()) {
                errors.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        } else {
            errors.put(ex.getErrorCode().getCode(), ex.getMessage());
        }

        return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
                .body(ApiErrorResponse.of(
                        ex.getErrorCode().getHttpStatus().value(),
                        request.getRequestURI(),
                        errors
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            String field = fieldError.getField().replaceAll("\\[.*?\\]", "");
            errors.putIfAbsent(field, fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest()
                .body(ApiErrorResponse.of(
                        400,
                        request.getRequestURI(),
                        errors
                ));
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