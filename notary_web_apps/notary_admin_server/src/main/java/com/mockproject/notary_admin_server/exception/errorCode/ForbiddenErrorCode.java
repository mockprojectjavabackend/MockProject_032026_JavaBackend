package com.mockproject.notary_admin_server.exception.errorCode;

import com.mockproject.notary_admin_server.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ForbiddenErrorCode implements ErrorCode {

    ACCESS_DENIED("403", "You do not have permission to access this resource", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}