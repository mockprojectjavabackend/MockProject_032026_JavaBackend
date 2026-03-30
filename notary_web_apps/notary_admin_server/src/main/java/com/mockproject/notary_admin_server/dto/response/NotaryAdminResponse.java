package com.mockproject.notary_admin_server.dto.response;

import com.mockproject.notary_common.constant.EmploymentType;
import com.mockproject.notary_common.constant.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


/**
 * NotaryAdminResponse
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 30-03-2026      PhamTam      create
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotaryAdminResponse {
    UUID id;
    String fullName;
    String email;
    String phone;
    String ssn;
    UserStatus status;
    String photoUrl;
    LocalDate dateOfBirth;
    LocalDate startDate;
    UUID userId;
    EmploymentType employmentType;
    String internalNotes;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String address;
    String city;
    String zipCode;
    List<StateResponse> states;
}
