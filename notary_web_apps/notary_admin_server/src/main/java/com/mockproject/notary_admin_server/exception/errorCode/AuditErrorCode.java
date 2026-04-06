package com.mockproject.notary_admin_server.exception.errorCode;

import org.springframework.http.HttpStatus;

import com.mockproject.notary_admin_server.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuditErrorCode implements ErrorCode {

        NOTARY_NOT_FOUND(
                        "AUDIT_001",
                        "Notary not found",
                        HttpStatus.NOT_FOUND),
        AUDIT_LOG_NOT_FOUND(
                        "AUDIT_002",
                        "Audit log entry not found",
                        HttpStatus.NOT_FOUND);

        private final String code;
        private final String message;
        private final HttpStatus httpStatus;
}
