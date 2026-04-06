package com.mockproject.notary_admin_server.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

/**
 * NotaryCreateResponseDTO
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 29-03-2026      TranMinh    create
 */
@Data
@Builder
public class NotaryCreateResponseDTO {
    private UUID id;
    private UUID userId;
    private String fullName;
    private String status;
    private String employmentType;
    private LocalDate startDate;
}
