package com.mockproject.notary_admin_server.exception.errorCode;

import org.springframework.http.HttpStatus;

import com.mockproject.notary_admin_server.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotaryErrorCode implements ErrorCode {
    NOTARY_NOT_FOUND("E5000", "Notary không tồn tại.", HttpStatus.NOT_FOUND),
    NOTARY_ALREADY_EXISTS("E5001", "Notary đã tồn tại.", HttpStatus.CONFLICT),
    NOTARY_INVALID_DATA("E5002", "Dữ liệu Notary không hợp lệ.", HttpStatus.BAD_REQUEST),
    NOTARY_OPERATION_FAILED("E5003", "Thao tác trên Notary thất bại.", HttpStatus.INTERNAL_SERVER_ERROR),
    SSN_ALREADY_EXISTS("E5004", "Số SSN đã tồn tại.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
