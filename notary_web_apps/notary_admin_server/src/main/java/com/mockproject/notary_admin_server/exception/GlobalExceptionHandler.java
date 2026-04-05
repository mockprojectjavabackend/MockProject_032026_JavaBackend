package com.mockproject.notary_admin_server.exception;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mockproject.notary_admin_server.dto.ApiErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiErrorResponse> handleAppException(AppException ex, HttpServletRequest request) {
        log.warn("AppException [{}] {}", ex.getErrorCode().getCode(), ex.getMessage());

        Map<String, String> errors;

        if (ex.hasDetails() && ex.getDetails() != null && !ex.getDetails().isEmpty()) {
            errors = ex.getDetails().entrySet().stream()
                    .collect(java.util.stream.Collectors.toMap(
                            Map.Entry::getKey, entry -> String.valueOf(entry.getValue())));
        } else {
            errors = Map.of(ex.getErrorCode().getCode(), ex.getMessage());
        }

        return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
                .body(ApiErrorResponse.of(ex.getErrorCode().getHttpStatus().value(), request.getRequestURI(), errors));
    }

    // Handle validation errors (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Validation failed for request: {}", request.getRequestURI());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.of(HttpStatus.BAD_REQUEST.value(), request.getRequestURI(), errors));
    }

    // Handle malformed JSON body (400)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Malformed request body for: {}", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.of(
                        HttpStatus.BAD_REQUEST.value(),
                        request.getRequestURI(),
                        Map.of("request_body", "Dữ liệu gửi lên không đúng định dạng JSON.")));
    }

    // Handle authorization errors (403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {
        log.warn("Access denied for request: {} from IP: {}", request.getRequestURI(), request.getRemoteAddr());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiErrorResponse.of(
                        HttpStatus.FORBIDDEN.value(),
                        request.getRequestURI(),
                        Map.of("access_denied", "Bạn không có quyền truy cập tài nguyên này.")));
    }

    private ResponseEntity<ApiErrorResponse> build(ErrorCode errorCode, HttpServletRequest request) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiErrorResponse.of(
                        errorCode.getHttpStatus().value(),
                        request.getRequestURI(),
                        Map.of(errorCode.getCode(), errorCode.getMessage())));
    }
}

