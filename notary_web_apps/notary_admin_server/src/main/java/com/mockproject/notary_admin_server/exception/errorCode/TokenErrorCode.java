package com.mockproject.notary_admin_server.exception.errorCode;

import org.springframework.http.HttpStatus;

import com.mockproject.notary_admin_server.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * TokenErrorCode — dành riêng cho Invitation Token (không phải JWT).
 * Range: E22xx
 */
@Getter
@RequiredArgsConstructor
public enum TokenErrorCode implements ErrorCode {
    // === E22xx: Invitation Token ===
    TOKEN_EXPIRED("E2200", "Liên kết đã hết hạn.", HttpStatus.GONE),
    TOKEN_INVALID("E2201", "Liên kết không hợp lệ.", HttpStatus.BAD_REQUEST),
    TOKEN_REVOKED("E2202", "Liên kết đã bị thu hồi.", HttpStatus.GONE),
    TOKEN_MALFORMED("E2203", "Liên kết không đúng định dạng.", HttpStatus.BAD_REQUEST),
    TOKEN_ALREADY_USED("E2204", "Liên kết đã được sử dụng trước đó.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
