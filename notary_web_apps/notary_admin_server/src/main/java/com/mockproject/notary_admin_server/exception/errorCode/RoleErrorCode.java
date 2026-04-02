package com.mockproject.notary_admin_server.exception.errorCode;

import com.mockproject.notary_admin_server.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoleErrorCode implements ErrorCode {
    ROLE_NOT_FOUND("E4000", "Không tìm thấy vai trò.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
