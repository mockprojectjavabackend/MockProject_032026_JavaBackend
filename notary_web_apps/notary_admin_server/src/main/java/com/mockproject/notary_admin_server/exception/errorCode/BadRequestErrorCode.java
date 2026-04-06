package com.mockproject.notary_admin_server.exception.errorCode;

import org.springframework.http.HttpStatus;

import com.mockproject.notary_admin_server.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BadRequestErrorCode implements ErrorCode {
    FILE_ERROR("400", "File is empty. Please upload file", HttpStatus.BAD_REQUEST),
    INVALID_FILE("400", "Invalid file.", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE("400", "File is too large.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
