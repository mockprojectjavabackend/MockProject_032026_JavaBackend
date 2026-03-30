package com.mockproject.notary_admin_server.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mockproject.notary_common.constant.EmploymentType;
import com.mockproject.notary_common.constant.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * NotaryUpdateRequestDTO
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 29-03-2026      TranMinh    create
 */
@Data
public class NotaryUpdateRequestDTO {
    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("photo_url")
    private String photoUrl;

    @Size(max = 15, message = "phone must not exceed 15 characters")
    private String phone;

    @Email(message = "email format is invalid")
    private String email;

    @JsonProperty("employment_type")
    private EmploymentType employmentType;

    @JsonProperty("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonProperty("internal_notes")
    private String internalNotes;

    private UserStatus status;

    @JsonProperty("residential_address")
    private String residentialAddress;
}
