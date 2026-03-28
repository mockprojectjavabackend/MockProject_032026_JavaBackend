package com.mockproject.notary_admin_server.dto.request;

import com.mockproject.notary_common.constant.EmploymentType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UpdateNotaryInfoRequest(
        @Size(min = 2, max = 128, message = "Full name must be between 2 and 128 characters")
        String fullName,

        @Pattern(regexp = "^\\+?[0-9]{10,20}$", message = "Invalid phone number format")
        String phone,

        @Size(max = 64, message = "Email must not exceed 64 characters")
        String email,

        @Size(max = 500, message = "Photo URL must not exceed 500 characters")
        String photoUrl,

        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @PastOrPresent(message = "Start date must not be in the future")
        LocalDate startDate,

        @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{4}$", message = "SSN format must be XXX-XX-XXXX")
        String ssn,

        EmploymentType employmentType,

        @Size(max = 1000, message = "Internal notes must not exceed 1000 characters")
        String internalNotes,

        @Size(max = 256, message = "Address must not exceed 256 characters")
        String address,

        @Size(max = 16, message = "Status must not exceed 16 characters")
        String status
)
{
}
