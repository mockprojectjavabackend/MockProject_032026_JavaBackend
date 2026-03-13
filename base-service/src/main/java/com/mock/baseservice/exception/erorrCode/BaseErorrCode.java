package com.mock.baseservice.exception.erorrCode;

import com.mock.baseservice.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BaseErorrCode implements ErrorCode {
    TEST_ERROR_CODE("400", "Tên lỗi", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
