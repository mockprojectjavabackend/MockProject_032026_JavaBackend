package com.mockproject.notary_admin_server.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        // Core - Custom Application Exception Handler
        @ExceptionHandler(AppException.class)
        public ResponseEntity<ApiSuccessResponse<Map<String, Object>>> handleAppException(AppException ex) {

                log.warn("AppException [{}] {}", ex.getErrorCode().getCode(), ex.getMessage());

                return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
                                .body(ApiSuccessResponse.success(
                                                ex.getErrorCode().getHttpStatus().value(), ex.getMessage(),
                                                ex.hasDetails() ? ex.getDetails() : null));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiSuccessResponse<Map<String, String>>> handleValidationException(
                        MethodArgumentNotValidException ex) {
                Map<String, String> errors = new LinkedHashMap<>();

                for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
                        errors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
                }

                return ResponseEntity.badRequest()
                                .body(ApiSuccessResponse.success(400, "Request validation failed", errors));
        }

        /* ================= UTIL ================= */

        private ResponseEntity<ApiSuccessResponse<Void>> build(ErrorCode errorCode) {
                return ResponseEntity.status(errorCode.getHttpStatus())
                                .body(ApiSuccessResponse.success(errorCode.getHttpStatus().value(),
                                                errorCode.getMessage(), null));
        }
}