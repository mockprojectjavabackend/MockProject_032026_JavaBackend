package com.mockproject.notary_admin_server.dto.request;

import com.mockproject.notary_common.constant.EmploymentType;
import com.mockproject.notary_common.constant.UserStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * UpdateNotaryInfoRequest
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      PhamTam      create
 */

@Getter
@Setter
public class UpdateNotaryInfoRequest{

        @Size(min = 2, max = 64, message = "Full name must be between 2 and 64 characters")
        private String fullName;

        @Size(max = 16, message = "Phone must not exceed 16 characters")
        @Pattern(regexp = "^\\+?[0-9]*$", message = "Phone must contain only digits")
        private String phone;

        @Email(message = "Invalid email format")
        @Size(max = 64, message = "Email must not exceed 64 characters")
        private String email;

        private String photoUrl;

        @Past(message = "Date of birth must be in the past")
        private LocalDate dateOfBirth;

        @PastOrPresent(message = "Start date must not be in the future")
        private LocalDate startDate;

        @Size(max = 32, message = "SSN must not exceed 32 characters")
        private String ssn;

        private EmploymentType employmentType;

        private String internalNotes;

        @Size(max = 128, message = "Address must not exceed 128 characters")
        private String address;

        private UserStatus status;
}
