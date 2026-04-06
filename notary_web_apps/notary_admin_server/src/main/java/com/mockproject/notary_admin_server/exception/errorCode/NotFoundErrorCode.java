package com.mockproject.notary_admin_server.exception.errorCode;

import org.springframework.http.HttpStatus;

import com.mockproject.notary_admin_server.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotFoundErrorCode implements ErrorCode {

    COMMISSION_NOT_FOUND("404", "Commission not found", HttpStatus.NOT_FOUND),
    NOTARY_NOT_FOUND("404", "Notary not found", HttpStatus.NOT_FOUND),
    STATE_NOT_FOUND("404", "State not found", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}