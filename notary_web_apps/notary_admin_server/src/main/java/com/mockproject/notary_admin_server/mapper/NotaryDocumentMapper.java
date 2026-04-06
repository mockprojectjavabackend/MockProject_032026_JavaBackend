package com.mockproject.notary_admin_server.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.mockproject.notary_common.constant.DocCategory;
import com.mockproject.notary_common.constant.VerifiedStatus;
import com.mockproject.notary_common.entity.notary.Notary;
import com.mockproject.notary_common.entity.notary.NotaryDocument;
import com.mockproject.notary_admin_server.dto.request.DocumentRequestDTO;
import com.mockproject.notary_admin_server.dto.response.DocumentResponseDTO;

/**
 * NotaryDocumentMapper
 *
 * @version 1.0
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 26-03-2026      AGENT       create
 */
@Component
public class NotaryDocumentMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String DATE_TIME_SUFFIX = "T00:00:00";

    //Map a NotaryDocument entity to DocumentResponseDTO.

    public DocumentResponseDTO toResponseDTO(NotaryDocument document) {
        return DocumentResponseDTO.builder()
                .id(document.getId().toString())
                .notaryId(document.getNotary() != null ? document.getNotary().getId().toString() : null)
                .docCategory(document.getDocCategory() != null ? document.getDocCategory().name() : null)
                .fileName(document.getFileName())
                .uploadDate(document.getUploadDate() != null ? document.getUploadDate().format(DATE_FORMATTER) : null)
                .status(document.getVerifiedStatus() != null ? document.getVerifiedStatus().name() : null)
                .version(String.valueOf(document.getVersion()))
                .isCurrentVersion(String.valueOf(document.isCurrentVersion()))
                .fileUrl(document.getFileUrl())
                .build();
    }

    //Map a DocumentRequestDTO and an owning Notary to a new NotaryDocument entity.
    public NotaryDocument toEntity(DocumentRequestDTO dto, Notary notary) {
        //parse DocCategory from string, default to null if unrecognised
        DocCategory docCategory = resolveDocCategory(dto.getDocCategory());

        //parse VerifiedStatus from string, default to PENDING
        VerifiedStatus verifiedStatus = VerifiedStatus.PENDING;
        if (dto.getStatus() != null) {
            try {
                verifiedStatus = VerifiedStatus.valueOf(dto.getStatus().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                verifiedStatus = VerifiedStatus.PENDING;
            }
        }

        //parse version number, default to 1
        int version = 1;
        if (dto.getVersion() != null) {
            try {
                version = (int) Double.parseDouble(dto.getVersion());
            } catch (NumberFormatException ignored) {
                version = 1;
            }
        }

        //parse isCurrentVersion boolean, default to true
        boolean isCurrentVersion = true;
        if (dto.getIsCurrentVersion() != null) {
            isCurrentVersion = Boolean.parseBoolean(dto.getIsCurrentVersion());
        }

        //parse uploadDate, default to now
        LocalDateTime uploadDate = parseUploadDate(dto.getUploadDate());

        return NotaryDocument.builder()
                .docCategory(docCategory)
                .fileName(dto.getFileName())
                .uploadDate(uploadDate)
                .verifiedStatus(verifiedStatus)
                .version(version)
                .isCurrentVersion(isCurrentVersion)
                .fileUrl(dto.getFileUrl())
                .notary(notary)
                .build();
    }

    // Update an existing NotaryDocument entity in-place from a DocumentRequestDTO.

    public void updateEntity(NotaryDocument document, DocumentRequestDTO dto) {
        if (dto.getDocCategory() != null) {
            DocCategory mappedCat = resolveDocCategory(dto.getDocCategory());
            if (mappedCat != null) {
                document.setDocCategory(mappedCat);
            }
        }

        if (dto.getFileName() != null) {
            document.setFileName(dto.getFileName());
        }

        if (dto.getUploadDate() != null && !dto.getUploadDate().isBlank()) {
            document.setUploadDate(parseUploadDate(dto.getUploadDate()));
        }

        if (dto.getFileUrl() != null) {
            document.setFileUrl(dto.getFileUrl());
        }

        if (dto.getStatus() != null) {
            try {
                document.setVerifiedStatus(VerifiedStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException ignored) {
            }
        }

        if (dto.getVersion() != null) {
            try {
                document.setVersion((int) Double.parseDouble(dto.getVersion()));
            } catch (NumberFormatException ignored) {
            }
        }

        if (dto.getIsCurrentVersion() != null) {
            document.setCurrentVersion(Boolean.parseBoolean(dto.getIsCurrentVersion()));
        }
    }

    /* ==================== private helpers ==================== */

    //Resolve a document category string to its enum value.
    private DocCategory resolveDocCategory(String categoryStr) {
        if (categoryStr == null) {
            return null;
        }
        String normalised = categoryStr.toLowerCase().trim().replace(" ", "_");
        return switch (normalised) {
            case "commission", "commission_cer" -> DocCategory.COMMISSION_CER;
            case "training", "training_cer"     -> DocCategory.TRAINING_CER;
            case "fingerprint", "fingersprint"  -> DocCategory.FINGERSPRINT;
            case "identity", "identity_verification" -> DocCategory.IDENTITY_VERIFICATION;
            default -> null;
        };
    }

    //Parse an upload date string (yyyy-MM-dd), defaulting to now on null/blank/invalid input.
    private LocalDateTime parseUploadDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return LocalDateTime.now();
        }
        try {
            return LocalDateTime.parse(dateStr + DATE_TIME_SUFFIX);
        } catch (Exception ignored) {
            return LocalDateTime.now();
        }
    }
}
