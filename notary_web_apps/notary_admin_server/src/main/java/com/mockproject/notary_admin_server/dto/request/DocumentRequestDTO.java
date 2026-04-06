package com.mockproject.notary_admin_server.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DocumentRequestDTO
 *
 * @version 1.0
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 26-03-2026      AXL24       create
 * 02-04-2026      AXL24       add Bean Validation constraints
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequestDTO {

    // Document category is mandatory — mapper falls back to null (invalid DB state) without it
    @NotBlank(message = "doc_category must not be blank")
    @JsonProperty("doc_category")
    private String docCategory;

    // File name is required and must fit within the database VARCHAR(255) column
    @NotBlank(message = "file_name must not be blank")
    @Size(max = 64, message = "file_name must not exceed 64 characters")
    @JsonProperty("file_name")
    private String fileName;

    // Upload date is optional; when provided it must follow the yyyy-MM-dd format
    // (mapper silently falls back to LocalDateTime.now() on invalid format)
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "upload_date must follow the format yyyy-MM-dd")
    @JsonProperty("upload_date")
    private String uploadDate;

    // Verified status is optional — mapper defaults to VerifiedStatus.PENDING when absent or unrecognised
    private String status;

    // Version is optional; when provided it must be a positive integer or decimal (e.g. "1", "1.0")
    @Pattern(regexp = "^\\d+(\\.\\d+)?$", message = "version must be a positive number (e.g. \"1\" or \"1.0\")")
    private String version;

    // isCurrentVersion is optional — Boolean.parseBoolean handles any string safely
    @JsonProperty("is_current_version")
    private String isCurrentVersion;

    // A document record without a file URL is semantically incomplete
    @NotBlank(message = "file_url must not be blank")
    @JsonProperty("file_url")
    private String fileUrl;
}
