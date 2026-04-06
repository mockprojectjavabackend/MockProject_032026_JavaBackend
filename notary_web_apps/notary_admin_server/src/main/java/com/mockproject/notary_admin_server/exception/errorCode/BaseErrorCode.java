package com.mockproject.notary_admin_server.exception.errorCode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import com.mockproject.notary_admin_server.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum BaseErrorCode implements ErrorCode {
    TEST_ERROR_CODE("400", "Tên lỗi", HttpStatus.BAD_REQUEST),
    NOTARY_NOT_FOUND("404", "Notary not found", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
