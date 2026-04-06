package com.mockproject.notary_admin_server.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * NotaryUpdateResponseDTO
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 29-03-2026      TranMinh    create
 */
@Data
@Builder
public class NotaryUpdateResponseDTO {
    private UUID id;
    private List<String> updatedFields;
    private LocalDateTime updatedAt;
}
