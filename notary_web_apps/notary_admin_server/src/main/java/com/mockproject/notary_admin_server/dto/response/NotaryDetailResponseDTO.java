package com.mockproject.notary_admin_server.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

/**
 * NotaryRepository
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 29-03-2026      TranMinh    create
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotaryDetailResponseDTO {
    private UUID id;
    private UUID userId;
    private String ssn;
    private String fullName;
    private LocalDate dateOfBirth;
    private String photoUrl;
    private String phone;
    private String email;
    private String employmentType;
    private LocalDate startDate;
    private String internalNotes;
    private String status;
    private String residentialAddress;
}
