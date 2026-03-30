package com.mockproject.notary_admin_server.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mockproject.notary_common.constant.EmploymentType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

/**
 * NotaryCreateRequestDTO
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 29-03-2026      TranMinh    create
 */
@Data
public class NotaryCreateRequestDTO {
    @NotNull(message = "user_id is required")
    @JsonProperty("user_id")
    private UUID userId;

    @NotBlank(message = "ssn is required")
    private String ssn;

    @NotBlank(message = "full_name is required")
    @JsonProperty("full_name")
    private String fullName;

    @NotNull(message = "date_of_birth is required")
    @JsonProperty("date_of_birth")
    @JsonFormat(pattern = "yyyy-MM-dd") // Ép chuẩn định dạng ngày tháng
    private LocalDate dateOfBirth;

    @JsonProperty("photo_url")
    private String photoUrl;

    @NotBlank(message = "phone is required")
    @Size(max = 15, message = "phone must not exceed 15 characters")
    private String phone;

    @NotBlank(message = "email is required")
    @Email(message = "email format is invalid")
    private String email;

    @NotNull(message = "employment_type is required")
    @JsonProperty("employment_type")
    private EmploymentType employmentType;

    @NotNull(message = "start_date is required")
    @JsonProperty("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonProperty("internal_notes")
    private String internalNotes;

    @NotBlank(message = "residential_address is required")
    @JsonProperty("residential_address")
    private String residentialAddress;
}
