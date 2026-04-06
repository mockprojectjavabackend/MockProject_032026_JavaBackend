package com.mockproject.notary_admin_server.exception;

import java.util.Map;
import java.util.UUID;

import com.mockproject.notary_admin_server.exception.errorCode.NotFoundErrorCode;

public class NotFoundException extends AppException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorCode errorCode, Map<String, Object> attributes) {
        super(errorCode, attributes);
    }

    public NotFoundException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public NotFoundException(
            ErrorCode errorCode,
            Map<String, Object> attributes,
            Map<String, Object> details,
            Throwable cause) {
        super(errorCode, attributes, details, cause);
    }

    public static NotFoundException commission() {
        return new NotFoundException(NotFoundErrorCode.COMMISSION_NOT_FOUND);
    }

    public static NotFoundException notary() {
        return new NotFoundException(NotFoundErrorCode.NOTARY_NOT_FOUND);
    }

    public static NotFoundException state() {
        return new NotFoundException(NotFoundErrorCode.STATE_NOT_FOUND);
    }
}