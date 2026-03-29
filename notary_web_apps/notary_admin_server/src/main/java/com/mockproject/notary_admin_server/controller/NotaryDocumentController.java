package com.mockproject.notary_admin_server.controller;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.request.DocumentRequestDTO;
import com.mockproject.notary_admin_server.dto.response.DocumentDeleteResponseDTO;
import com.mockproject.notary_admin_server.dto.response.DocumentResponseDTO;
import com.mockproject.notary_admin_server.dto.response.DocumentUploadResponseDTO;
import com.mockproject.notary_admin_server.dto.response.PagedDocumentResponseDTO;
import com.mockproject.notary_admin_server.service.NotaryDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * NotaryDocumentController
 *
 * @version 1.0
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 26-03-2026      AXL24       create
 */
@RestController
@RequestMapping("/api/notaries/{id}/documents")
@RequiredArgsConstructor
public class NotaryDocumentController {

    private final NotaryDocumentService notaryDocumentService;

    /**
     * Retrieve a paginated list of documents for a notary with optional filters.
     */
    @GetMapping
    public ResponseEntity<ApiSuccessResponse<PagedDocumentResponseDTO>> getDocuments(
            @PathVariable UUID id,
            @RequestParam(required = false) String status,
            @RequestParam(name = "date_range", required = false) String dateRange,
            @RequestParam(name = "document_type", required = false) String documentType,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        PagedDocumentResponseDTO result = notaryDocumentService.getDocuments(
                id, status, dateRange, documentType, search, page, limit, offset
        );
        return ResponseEntity.ok(ApiSuccessResponse.ok(result));
    }

    /**
     * Retrieve the details of a single document.
     */
    @GetMapping("/{doc_id}")
    public ResponseEntity<ApiSuccessResponse<DocumentResponseDTO>> getDocumentDetail(
            @PathVariable UUID id,
            @PathVariable UUID doc_id
    ) {
        DocumentResponseDTO result = notaryDocumentService.getDocumentDetail(id, doc_id);
        return ResponseEntity.ok(ApiSuccessResponse.ok(result));
    }

    /**
     * Create a new document record for a notary.
     */
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<DocumentResponseDTO>> createDocument(
            @PathVariable UUID id,
            @RequestBody DocumentRequestDTO dto
    ) {
        DocumentResponseDTO result = notaryDocumentService.createDocument(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiSuccessResponse.created(result));
    }

    /**
     * Upload a file for a notary document.
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiSuccessResponse<DocumentUploadResponseDTO>> uploadDocument(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file
    ) {
        DocumentUploadResponseDTO result = notaryDocumentService.uploadDocument(id, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiSuccessResponse.created(result));
    }

    /**
     * Update an existing document record.
     */
    @PutMapping("/{doc_id}")
    public ResponseEntity<ApiSuccessResponse<DocumentResponseDTO>> updateDocument(
            @PathVariable UUID id,
            @PathVariable UUID doc_id,
            @RequestBody DocumentRequestDTO dto
    ) {
        DocumentResponseDTO result = notaryDocumentService.updateDocument(id, doc_id, dto);
        return ResponseEntity.ok(ApiSuccessResponse.ok(result));
    }

    /**
     * Permanently delete a document (removes the row from DB).
     */
    @DeleteMapping("/{doc_id}")
    public ResponseEntity<ApiSuccessResponse<DocumentDeleteResponseDTO>> deleteDocument(
            @PathVariable UUID id,
            @PathVariable UUID doc_id
    ) {
        DocumentDeleteResponseDTO result = notaryDocumentService.deleteDocument(id, doc_id);
        return ResponseEntity.ok(ApiSuccessResponse.ok(result));
    }
}
