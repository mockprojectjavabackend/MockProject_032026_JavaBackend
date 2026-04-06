package com.mockproject.notary_admin_server.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

/**
 * NotaryResponseDTO
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      TranMinh    create
 */
@Data
@Builder
public class NotaryResponseDTO {
    private UUID id;
    private UUID userId;
    private String fullName;
    private String photoUrl;
    private String email;
    private String phone;
    private String employmentType;
    private String status;
    private LocalDate startDate;
    private String residentialAddress;
}
