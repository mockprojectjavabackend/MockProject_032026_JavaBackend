package com.mockproject.notary_admin_server.exception.errorCode;

import com.mockproject.notary_admin_server.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * DocumentErrorCode
 *
 * @version 1.0
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 26-03-2026      AGENT       create
 * 29-03-2026      AGENT       edit
 */
@Getter
@RequiredArgsConstructor
public enum DocumentErrorCode implements ErrorCode {

    // ── Notary ────────────────────────────────────────────────────────────────
    NOTARY_NOT_FOUND(
            "DOC_001",
            "Notary with id= {id} not found",
            HttpStatus.NOT_FOUND),

    // ── Document ──────────────────────────────────────────────────────────────
    DOCUMENT_NOT_FOUND(
            "DOC_002",
            "Document with id= {id} not found",
            HttpStatus.NOT_FOUND),

    DOCUMENT_ALREADY_DELETED(
            "DOC_003",
            "Document with id= {id} has already been deleted",
            HttpStatus.CONFLICT),

    DOCUMENT_DOES_NOT_BELONG_TO_NOTARY(
            "DOC_004",
            "Document with id= {id} does not belong to notary with id= {notaryId}",
            HttpStatus.FORBIDDEN),

    // ── Upload ────────────────────────────────────────────────────────────────
    UPLOAD_EMPTY_FILE(
            "DOC_005",
            "Uploaded file is empty or missing",
            HttpStatus.BAD_REQUEST),

    UPLOAD_INVALID_FILE(
            "DOC_006",
            "Invalid file: {reason}",
            HttpStatus.BAD_REQUEST),

    UPLOAD_FAILED(
            "DOC_007",
            "File upload failed due to a server error",
            HttpStatus.INTERNAL_SERVER_ERROR),



    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}