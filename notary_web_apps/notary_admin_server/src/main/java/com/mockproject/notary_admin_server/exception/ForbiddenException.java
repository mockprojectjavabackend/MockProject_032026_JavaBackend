package com.mockproject.notary_admin_server.exception;

import java.util.Map;

import com.mockproject.notary_admin_server.exception.errorCode.ForbiddenErrorCode;

public class ForbiddenException extends AppException {

    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ForbiddenException(ErrorCode errorCode, Map<String, Object> attributes) {
        super(errorCode, attributes);
    }

    public ForbiddenException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ForbiddenException(
            ErrorCode errorCode,
            Map<String, Object> attributes,
            Map<String, Object> details,
            Throwable cause) {
        super(errorCode, attributes, details, cause);
    }

    public static ForbiddenException accessDenied() {
        return new ForbiddenException(ForbiddenErrorCode.ACCESS_DENIED);
    }

}