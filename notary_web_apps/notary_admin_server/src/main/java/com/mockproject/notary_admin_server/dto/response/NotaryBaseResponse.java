package com.mockproject.notary_admin_server.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import com.mockproject.notary_common.constant.UserStatus;

/**
 * NotaryBaseResponse
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 02-04-2026      PhamTam      create

 */
@Data
@SuperBuilder
public abstract class NotaryBaseResponse {
    UUID id;
    String fullName;
    String email;
    String phone;
    UserStatus status;
    String photoUrl;
    LocalDate dateOfBirth;
    LocalDate startDate;
    UUID userId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String address;
    String city;
    String zipCode;
    List<StateResponse> states;
}
