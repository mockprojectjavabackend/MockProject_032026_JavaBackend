package com.mockproject.notary_admin_server.exception.errorCode;

import org.springframework.http.HttpStatus;

import com.mockproject.notary_admin_server.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailErrorCode implements ErrorCode {
    // === E6xxx: Email ===
    EMAIL_SEND_FAILED("E6000", "Gửi email thất bại.", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_TEMPLATE_NOT_FOUND("E6001", "Không tìm thấy template email.", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_INVALID_ADDRESS("E6002", "Địa chỉ email không hợp lệ.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
