package com.mockproject.notary_admin_server.exception;

import java.util.Map;

import com.mockproject.notary_admin_server.exception.errorCode.BadRequestErrorCode;
import com.mockproject.notary_admin_server.exception.errorCode.NotFoundErrorCode;

public class BadRequestException extends AppException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadRequestException(ErrorCode errorCode, Map<String, Object> attributes) {
        super(errorCode, attributes);
    }

    public BadRequestException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public BadRequestException(
            ErrorCode errorCode,
            Map<String, Object> attributes,
            Map<String, Object> details,
            Throwable cause) {
        super(errorCode, attributes, details, cause);
    }

    public static BadRequestException file() {
        return new BadRequestException(BadRequestErrorCode.FILE_ERROR);
    }

    public static BadRequestException invalidFile() {
        return new BadRequestException(BadRequestErrorCode.INVALID_FILE);
    }

    public static BadRequestException fileTooLarge() {
        return new BadRequestException(BadRequestErrorCode.FILE_TOO_LARGE);
    }
}
