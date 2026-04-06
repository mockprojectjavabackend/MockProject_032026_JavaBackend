package com.mockproject.notary_admin_server.dto.request;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import com.mockproject.notary_common.constant.EmploymentType;
import com.mockproject.notary_common.constant.UserStatus;

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

        @NotBlank(message = "fullName is required")
        @Size(min = 2, max = 255, message = "Full name must be between 2 and 64 characters")
        private String fullName;

        @NotBlank(message = "Phone is required")
        @Size(max = 16, message = "Phone must not exceed 16 characters")
        @Pattern(
                regexp = "^\\+?[0-9-]+$",
                message = "Phone must contain only digits, '+' or '-'"
        )
        private String phone;

        @NotBlank(message = "Phone is required")
        @Email(message = "Invalid email format")
        @Size(max = 64, message = "Email must not exceed 64 characters")
        private String email;

        @NotBlank(message = "Phone is required")
        private String photoUrl;

        @Past(message = "Date of birth must be in the past")
        private LocalDate dateOfBirth;

        @PastOrPresent(message = "Start date must not be in the future")
        private LocalDate startDate;

        @Size(max = 32, message = "SSN must not exceed 32 characters")
        private String ssn;

        private EmploymentType employmentType;

        private String internalNotes;

        @NotBlank(message = "address is required")
        @Size(max = 128, message = "Address must not exceed 128 characters")
        private String address;

        @NotBlank(message = "city is required")
        @Size(max = 255, message = "Address must not exceed 128 characters")
        private String city;

        @NotBlank(message = "Zip code is required")
        @Pattern(
                regexp = "^\\d{5}(-\\d{4})?$",
                message = "Zip code must be 5 digits or in format 12345-6789"
        )
        @Size(max = 128, message = "Address must not exceed 128 characters")
        private String zipCode;

        private UserStatus status;

        private List<UUID> states;
}
