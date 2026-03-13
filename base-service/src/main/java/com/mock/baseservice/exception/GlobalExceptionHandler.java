package com.mock.baseservice.exception;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mock.baseservice.dto.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Core - Custom Application Exception Handler
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleAppException(AppException ex) {

        log.warn("AppException [{}] {}", ex.getErrorCode().getCode(), ex.getMessage());

        return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
                .body(ApiResponse.error(
                        ex.getErrorCode().getCode(), ex.getMessage(), ex.hasDetails() ? ex.getDetails() : null));
    }

    /* ================= UTIL ================= */

    private ResponseEntity<ApiResponse<Void>> build(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }
}
