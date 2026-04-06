package com.mockproject.notary_admin_server.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * PaginationDTO
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
public class PaginationDTO {

    private int page;

    private int limit;

    private long total;

    @JsonProperty("total_pages")
    private int totalPages;
}
