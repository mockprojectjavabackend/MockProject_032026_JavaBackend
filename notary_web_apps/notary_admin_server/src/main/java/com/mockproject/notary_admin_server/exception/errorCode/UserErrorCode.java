package com.mockproject.notary_admin_server.exception.errorCode;

import org.springframework.http.HttpStatus;

import com.mockproject.notary_admin_server.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND("E3000", "Không tìm thấy người dùng.", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS("E3100", "Email này đã được sử dụng.", HttpStatus.CONFLICT),
    PHONE_NUMBER_ALREADY_EXISTS("E3101", "Số điện thoại này đã được sử dụng.", HttpStatus.CONFLICT),

    USER_ACCOUNT_INACTIVE("E3200", "Tài khoản chưa được kích hoạt.", HttpStatus.FORBIDDEN),
    USER_ACCOUNT_SUSPENDED("E3201", "Tài khoản đã bị tạm ngưng.", HttpStatus.FORBIDDEN),
    USER_ACCOUNT_BLOCKED("E3202", "Tài khoản đã bị khóa.", HttpStatus.FORBIDDEN),
    USER_ACCOUNT_DELETED("E3203", "Tài khoản không còn khả dụng.", HttpStatus.FORBIDDEN),
    USER_STATUS_INVALID("E3204", "Trạng thái tài khoản không hợp lệ.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
