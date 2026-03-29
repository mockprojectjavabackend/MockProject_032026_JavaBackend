package com.mockproject.notary_admin_server.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequestDTO {


    @JsonProperty("doc_category")
    private String docCategory;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("upload_date")
    private String uploadDate;

    private String status;

    private String version;

    @JsonProperty("is_current_version")
    private String isCurrentVersion;

    @JsonProperty("file_url")
    private String fileUrl;
}
