package com.mockproject.notary_admin_server.mapper;

import com.mockproject.notary_common.constant.DocCategory;
import com.mockproject.notary_common.constant.VerifiedStatus;
import com.mockproject.notary_common.entity.notary.Notary;
import com.mockproject.notary_common.entity.notary.NotaryDocument;
import com.mockproject.notary_admin_server.dto.request.DocumentRequestDTO;
import com.mockproject.notary_admin_server.dto.response.DocumentResponseDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Map a NotaryDocument entity to DocumentResponseDTO.
     *
     * @param document the source entity
     * @return populated DocumentResponseDTO
     */
    public DocumentResponseDTO toResponseDTO(NotaryDocument document) {
        return DocumentResponseDTO.builder()
                .id(document.getId().toString())
                .notaryId(document.getNotary() != null ? document.getNotary().getId().toString() : null)
                .docCategory(document.getDocCategory() != null ? document.getDocCategory().name() : null)
                .fileName(document.getFileName())
                .uploadDate(document.getUploadDate() != null ? document.getUploadDate().format(DATE_FORMATTER) : null)
                .size(document.getSize())
                .status(document.getVerifiedStatus() != null ? document.getVerifiedStatus().name().toLowerCase() : null)
                .version(String.valueOf(document.getVersion()))
                .isCurrentVersion(String.valueOf(document.isCurrentVersion()))
                .fileUrl(document.getFileUrl())
                .build();
    }

    /**
     * Map a DocumentRequestDTO and an owning Notary to a new NotaryDocument entity.
     *
     * @param dto    the request body
     * @param notary the owning notary entity
     * @return new NotaryDocument entity (not yet persisted)
     */
    public NotaryDocument toEntity(DocumentRequestDTO dto, Notary notary) {
        //parse DocCategory from string, default to null if unrecognised
        DocCategory docCategory = null;
        if (dto.getDocCategory() != null) {
            String catStr = dto.getDocCategory().toLowerCase().trim().replace(" ", "_");
            docCategory = switch (catStr) {
                case "commission", "commission_cer" -> DocCategory.COMMISSION_CER;
                case "training", "training_cer" -> DocCategory.TRAINING_CER;
                case "fingerprint", "fingersprint" -> DocCategory.FINGERSPRINT;
                case "identity", "identity_verification" -> DocCategory.IDENTITY_VERIFICATION;
                default -> null;
            };
        }

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
        LocalDateTime uploadDate = LocalDateTime.now();
        if (dto.getUploadDate() != null && !dto.getUploadDate().isBlank()) {
            try {
                uploadDate = LocalDateTime.parse(dto.getUploadDate() + "T00:00:00");
            } catch (Exception ignored) {
                uploadDate = LocalDateTime.now();
            }
        }

        return NotaryDocument.builder()
                .docCategory(docCategory)
                .fileName(dto.getFileName())
                .uploadDate(uploadDate)
                .size(dto.getSize())
                .verifiedStatus(verifiedStatus)
                .version(version)
                .isCurrentVersion(isCurrentVersion)
                .fileUrl(dto.getFileUrl())
                .notary(notary)
                .build();
    }

    /**
     * Update an existing NotaryDocument entity in-place from a DocumentRequestDTO.
     *
     * @param document the entity to update
     * @param dto      the new data
     */
    public void updateEntity(NotaryDocument document, DocumentRequestDTO dto) {
        if (dto.getDocCategory() != null) {
            String catStr = dto.getDocCategory().toLowerCase().trim().replace(" ", "_");
            DocCategory mappedCat = switch (catStr) {
                case "commission", "commission_cer" -> DocCategory.COMMISSION_CER;
                case "training", "training_cer" -> DocCategory.TRAINING_CER;
                case "fingerprint", "fingersprint" -> DocCategory.FINGERSPRINT;
                case "identity", "identity_verification" -> DocCategory.IDENTITY_VERIFICATION;
                default -> null;
            };
            if (mappedCat != null) {
                document.setDocCategory(mappedCat);
            }
        }

        if (dto.getFileName() != null) {
            document.setFileName(dto.getFileName());
        }

        if (dto.getUploadDate() != null && !dto.getUploadDate().isBlank()) {
            try {
                document.setUploadDate(LocalDateTime.parse(dto.getUploadDate() + "T00:00:00"));
            } catch (Exception ignored) {
            }
        }

        if (dto.getSize() != null) {
            document.setSize(dto.getSize());
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

        if (dto.getFileUrl() != null) {
            document.setFileUrl(dto.getFileUrl());
        }
    }

    /**
     * Format a LocalDateTime as an ISO-8601 string for API responses.
     *
     * @param dateTime the datetime to format
     * @return formatted string, or null if input is null
     */
    public String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATETIME_FORMATTER) + "Z";
    }
}
