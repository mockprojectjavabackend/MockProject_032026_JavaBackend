package com.mockproject.notary_admin_server.service.impl;

import com.mockproject.notary_admin_server.dto.request.DocumentRequestDTO;
import com.mockproject.notary_admin_server.dto.response.DocumentDeleteResponseDTO;
import com.mockproject.notary_admin_server.dto.response.DocumentResponseDTO;
import com.mockproject.notary_admin_server.dto.response.DocumentUploadResponseDTO;
import com.mockproject.notary_admin_server.dto.response.PagedDocumentResponseDTO;
import com.mockproject.notary_admin_server.dto.response.PaginationDTO;
import com.mockproject.notary_admin_server.exception.AppException;
import com.mockproject.notary_admin_server.exception.errorCode.DocumentErrorCode;
import com.mockproject.notary_admin_server.mapper.NotaryDocumentMapper;
import com.mockproject.notary_admin_server.repository.NotaryDocumentRepository;
import com.mockproject.notary_admin_server.repository.NotaryRepository;
import com.mockproject.notary_admin_server.service.NotaryDocumentAuthorizationService;
import com.mockproject.notary_admin_server.service.NotaryDocumentService;
import com.mockproject.notary_admin_server.service.NotaryDocumentStorageService;
import com.mockproject.notary_common.constant.DocCategory;
import com.mockproject.notary_common.constant.VerifiedStatus;
import com.mockproject.notary_common.entity.notary.Notary;
import com.mockproject.notary_common.entity.notary.NotaryDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * NotaryDocumentServiceImpl
 *
 * @version 1.0
 *
 *          Modification Logs:
 *          DATE AUTHOR DESCRIPTION
 *          -----------------------------------------------
 *          26-03-2026 AXL24 create
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotaryDocumentServiceImpl implements NotaryDocumentService {


    // upload directory path (relative to working directory)
    @Value("${app.upload.document.dir}")
    private String uploadDir;
    private final NotaryDocumentStorageService documentStorageService;
    private final NotaryDocumentRepository notaryDocumentRepository;
    private final NotaryRepository notaryRepository;
    private final NotaryDocumentMapper notaryDocumentMapper;
    private final NotaryDocumentAuthorizationService notaryDocAuthService;

    /**
     * Retrieve a paginated list of documents, applying all optional filters.
     *
     * @param notaryId     the notary UUID
     * @param status       optional status filter
     * @param dateRange    optional date range keyword
     * @param documentType optional document category filter
     * @param search       optional file name keyword
     * @param page         1-based page number
     * @param limit        page size
     * @param offset       record offset (overrides page calculation when > 0)
     * @return paginated document list response
     */
    @Override
    public PagedDocumentResponseDTO getDocuments(
            UUID notaryId,
            String status,
            String dateRange,
            String documentType,
            String search,
            int page,
            int limit,
            int offset) {
        // validate notary exists
        findNotary(notaryId);

        // resolve VerifiedStatus filter
        VerifiedStatus verifiedStatus = resolveVerifiedStatus(status);

        // resolve DocCategory filter
        DocCategory docCategory = resolveDocCategory(documentType);

        // resolve date range bounds
        LocalDateTime fromDate = resolveFromDate(dateRange);
        LocalDateTime toDate = resolveToDate(dateRange);

        // resolve page index (0-based for Spring Data)
        int pageIndex = resolvePageIndex(page, limit, offset);

        Pageable pageable = PageRequest.of(pageIndex, limit);

        Page<NotaryDocument> resultPage = notaryDocumentRepository.findAllByFilters(
                notaryId, verifiedStatus, docCategory,
                (search != null && !search.isBlank()) ? search : null,
                fromDate, toDate, pageable);

        // map entities to DTOs
        List<DocumentResponseDTO> items = resultPage.getContent()
                .stream()
                .map(notaryDocumentMapper::toResponseDTO)
                .collect(Collectors.toList());

        PaginationDTO pagination = PaginationDTO.builder()
                .page(pageIndex + 1)
                .limit(limit)
                .total(resultPage.getTotalElements())
                .totalPages(resultPage.getTotalPages())
                .build();

        return PagedDocumentResponseDTO.builder()
                .items(items)
                .pagination(pagination)
                .build();
    }

    /**
     * Retrieve a single active document by ID, scoped to the given notary.
     *
     * @param notaryId the notary UUID
     * @param docId    the document UUID
     * @return document response DTO
     */
    @Override
    public DocumentResponseDTO getDocumentDetail(UUID notaryId, UUID docId) {
        // validate notary exists
        findNotary(notaryId);
        // find document by docId, ensuring it's active and belongs to the notary
        NotaryDocument document = notaryDocAuthService.getActiveDocumentOwnedBy(notaryId, docId);


        return notaryDocumentMapper.toResponseDTO(document);
    }

    /**
     * Create and persist a new document for the given notary.
     *
     * @param notaryId the notary UUID
     * @param dto      the request body
     * @return created document response DTO
     */
    @Override
    @Transactional
    public DocumentResponseDTO createDocument(UUID notaryId, DocumentRequestDTO dto) {
        // validate notary exists
        Notary notary = findNotary(notaryId);

        // map request to entity and persist
        NotaryDocument document = notaryDocumentMapper.toEntity(dto, notary);
        NotaryDocument saved = notaryDocumentRepository.save(document);

        log.info("Created document [{}] for notary [{}]", saved.getId(), notaryId);

        return notaryDocumentMapper.toResponseDTO(saved);
    }

    /**
     * Upload a file to the local uploads directory and return its URL.
     *
     * @param notaryId the notary UUID
     * @param file     the multipart file
     * @return upload response containing the stored file URL
     */
    @Override
    public DocumentUploadResponseDTO uploadDocument(UUID notaryId, MultipartFile file) {
        findNotary(notaryId);

        String subDir  = uploadDir + "/" + notaryId;   // e.g. uploads/documents/{notaryId}
        String fileUrl = documentStorageService.uploadFile(file, subDir);

        return DocumentUploadResponseDTO.builder()
                .fileUrl(fileUrl)
                .build();
    }

    /**
     * Update an existing document record in-place.
     *
     * @param notaryId the notary UUID
     * @param docId    the document UUID
     * @param dto      the updated fields
     * @return updated document response DTO
     */
    @Override
    @Transactional
    public DocumentResponseDTO updateDocument(UUID notaryId, UUID docId, DocumentRequestDTO dto) {
        // validate notary exists
        findNotary(notaryId);

        // find document by docId, ensuring it's active and belongs to the notary
        NotaryDocument document = notaryDocAuthService.getActiveDocumentOwnedBy(notaryId, docId);

        // apply updates in-place and save
        notaryDocumentMapper.updateEntity(document, dto);
        NotaryDocument updated = notaryDocumentRepository.save(document);

        log.info("Updated document [{}] for notary [{}]", docId, notaryId);

        return notaryDocumentMapper.toResponseDTO(updated);
    }

    /**
     * Soft-delete a document by setting its deletedAt timestamp.
     *
     * @param notaryId the notary UUID
     * @param docId    the document UUID
     * @return soft-delete confirmation including id, status, and deleted_at
     */
    @Override
    @Transactional
    public DocumentDeleteResponseDTO deleteDocument(UUID notaryId, UUID docId) {
        // validate notary exists
        findNotary(notaryId);

        // find document by docId, ensuring it's active and belongs to the notary
        NotaryDocument document = notaryDocAuthService.getActiveDocumentOwnedBy(notaryId, docId);

        // soft-delete: stamp deletedAt and persist
        LocalDateTime now = LocalDateTime.now();
        document.setDeletedAt(now);
        notaryDocumentRepository.save(document);

        log.info("Soft-deleted document [{}] for notary [{}]", docId, notaryId);

        return DocumentDeleteResponseDTO.builder()
                .id(docId.toString())
                .status("INACTIVE")
                .deletedAt(notaryDocumentMapper.formatDateTime(now))
                .build();
    }


    /* ==================== private helpers ==================== */


    /**
     * Retrieve a Notary entity or throw NOTARY_NOT_FOUND.
     */
    private Notary findNotary(UUID notaryId) {
        return notaryRepository.findById(notaryId)
                .orElseThrow(() -> new AppException(DocumentErrorCode.NOTARY_NOT_FOUND, Map.of("id", notaryId)));
    }

    /**
     * Resolve a status string to VerifiedStatus enum, or null if not provided.
     */
    private VerifiedStatus resolveVerifiedStatus(String status) {
        final Map<String, VerifiedStatus> VERIFIED_STATUS = Map.of(
                "approved", VerifiedStatus.APPROVED,
                "pending", VerifiedStatus.PENDING,
                "rejected", VerifiedStatus.REJECTED
        );

        if (status == null || status.isBlank()) return null;
        return VERIFIED_STATUS.get(status.toLowerCase());
    }

    /**
     * Resolve a document type string to DocCategory enum, or null if not provided.
     */
    private DocCategory resolveDocCategory(String documentType) {
        final Map<String, DocCategory> DOC_CATEGORY = Map.of(
                "commission", DocCategory.COMMISSION_CER,
                "training", DocCategory.TRAINING_CER,
                "fingerprint", DocCategory.FINGERSPRINT,
                "identity", DocCategory.IDENTITY_VERIFICATION
        );
        if (documentType == null || documentType.isBlank()) return null;

        return DOC_CATEGORY.get(documentType.toLowerCase());
    }

    /**
     * Resolve dateRange string to a fromDate bound.
     */
    private LocalDateTime resolveFromDate(String dateRange) {
        final Map<String, LocalDateTime> DATE_RANGE = Map.of(
                "last_7_days", LocalDateTime.now().minusDays(7),
                "last_30_days", LocalDateTime.now().minusDays(30),
                "last_90_days", LocalDateTime.now().minusDays(90)
        );
        if (dateRange == null || dateRange.isBlank() || "custom".equalsIgnoreCase(dateRange)) return null;

        return DATE_RANGE.get(dateRange.toLowerCase());
    }

    /**
     * Resolve dateRange string to a toDate bound (always now for built-in ranges).
     */
    private LocalDateTime resolveToDate(String dateRange) {
        if (dateRange == null || dateRange.isBlank() || "custom".equalsIgnoreCase(dateRange)) {
            return null;
        }
        return LocalDateTime.now();
    }

    /**
     * Convert 1-based page number and optional offset to a 0-based Spring Data page
     * index.
     */
    private int resolvePageIndex(int page, int limit, int offset) {
        return offset > 0
                ? offset / limit
                : Math.max(0, page - 1);

    }
}
