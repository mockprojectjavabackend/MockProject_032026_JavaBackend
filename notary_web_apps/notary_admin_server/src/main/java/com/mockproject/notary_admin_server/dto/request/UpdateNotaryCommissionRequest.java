package com.mockproject.notary_admin_server.dto.request;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import com.mockproject.notary_common.constant.AuthorityType;
import com.mockproject.notary_common.constant.CommissionStatus;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UpdateNotaryCommissionRequest
 *
 * @version 1.0
 * @date 29-03-2026
 *       <p>
 *       Modification Logs:
 *       DATE AUTHOR DESCRIPTION
 *       -----------------------------------------------
 *       29-03-2026 HuyenThuong UpdateNotaryCommissionRequest
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNotaryCommissionRequest {
    @NotNull(message = "State is required")
    private UUID commissionStateId;

    @NotBlank(message = "Commission number is required")
    @Size(max = 64, message = "Commission number must not exceed 64 characters")
    private String commissionNumber;

    @NotNull(message = "Issue date is required")
    @PastOrPresent(message = "Issue date cannot be in the future")
    private LocalDate issueDate;

    @NotNull(message = "Expiration date is required")
    @Future(message = "Expiration date must be in the future")
    private LocalDate expirationDate;

    @Future(message = "Expected renewal date must be in the future")
    private LocalDate expectedRenewalDate;

    private CommissionStatus status;

    private Boolean isRenewalApplied;
    private String fileUrl;

    @Size(min = 1, message = "At least one authority type is required")
    private Set<AuthorityType> authorityTypes;

    @AssertTrue(message = "Expiration date must be after issue date")
    public boolean isValidDateRange() {
        if (issueDate == null || expirationDate == null)
            return true;
        return expirationDate.isAfter(issueDate);
    }

    @AssertTrue(message = "Expected renewal date must be before expiration date")
    public boolean isValidRenewalDate() {
        if (expectedRenewalDate == null || expirationDate == null)
            return true;
        return expectedRenewalDate.isBefore(expirationDate);
    }

    @AssertTrue(message = "Expected renewal date must be after issue date")
    public boolean isValidRenewalAfterIssue() {
        if (expectedRenewalDate == null || issueDate == null)
            return true;
        return expectedRenewalDate.isAfter(issueDate);
    }
}
