package com.mockproject.notary_admin_server.dto.response;

import com.mockproject.notary_common.constant.EmploymentType;
import com.mockproject.notary_common.constant.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
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
}
