package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_admin_server.dto.request.DocumentRequestDTO;
import com.mockproject.notary_admin_server.dto.response.DocumentDeleteResponseDTO;
import com.mockproject.notary_admin_server.dto.response.DocumentResponseDTO;
import com.mockproject.notary_admin_server.dto.response.DocumentUploadResponseDTO;
import com.mockproject.notary_admin_server.dto.response.PagedDocumentResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * NotaryDocumentService
 *
 * @version 1.0
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 26-03-2026      AXL24       create
 */
public interface NotaryDocumentService {


    PagedDocumentResponseDTO getDocuments(
            UUID notaryId,
            String status,
            String dateRange,
            String documentType,
            String search,
            int page,
            int limit,
            int offset
    );

    DocumentResponseDTO getDocumentDetail(UUID notaryId, UUID docId);


    DocumentResponseDTO createDocument(UUID notaryId, DocumentRequestDTO dto);


    DocumentUploadResponseDTO uploadDocument(UUID notaryId, MultipartFile file);


    DocumentResponseDTO updateDocument(UUID notaryId, UUID docId, DocumentRequestDTO dto);


    DocumentDeleteResponseDTO deleteDocument(UUID notaryId, UUID docId);
}
