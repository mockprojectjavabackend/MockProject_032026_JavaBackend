package com.mockproject.notary_admin_server.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.mockproject.notary_common.constant.CommissionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommissionDetailResponse {

    private UUID id;
    private String commissionNumber;
    private String commissionState;

    private LocalDate issueDate;
    private LocalDate expirationDate;
    private LocalDate expectedRenewalDate;

    private CommissionStatus status;
    private Boolean isRenewalApplied;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}