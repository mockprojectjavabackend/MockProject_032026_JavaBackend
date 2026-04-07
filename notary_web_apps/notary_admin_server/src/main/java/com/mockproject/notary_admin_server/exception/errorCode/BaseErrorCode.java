package com.mockproject.notary_admin_server.exception.errorCode;

import org.springframework.http.HttpStatus;

import com.mockproject.notary_admin_server.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum BaseErrorCode implements ErrorCode {
    TEST_ERROR_CODE("400", "Tên lỗi", HttpStatus.BAD_REQUEST),
    NOTARY_CAPABILITY_NOT_FOUND("4000", "Notary capability not found with id: {notaryId}", HttpStatus.NOT_FOUND),
    NOTARY_AVAILABILITY_NOT_FOUND("4001", "Notary availability not found with id: {notaryId}", HttpStatus.NOT_FOUND),
    NOTARY_SERVICE_AREA_NOT_FOUND("4002", "Notary service area not found with id: {notaryId}", HttpStatus.NOT_FOUND),
    NOTARY_SERVICE_AREA_COUNTY_NOT_FOUND("4003", "Notary service county area not found", HttpStatus.NOT_FOUND),
    INVALID_LANGUAGE("4004", "Some languages are invalid", HttpStatus.BAD_REQUEST),
    NOTARY_CAPABILITY_EXISTED("4005","Notary capability already exists", HttpStatus.BAD_REQUEST),
    NOTARY_AVAILABILITY_EXISTED("4006","Notary availability", HttpStatus.BAD_REQUEST),
    NOTARY_NOT_FOUND("4007","Notary not found", HttpStatus.BAD_REQUEST),
    STATE_NOT_FOUND("4008","State not found", HttpStatus.BAD_REQUEST),
    ;


    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
