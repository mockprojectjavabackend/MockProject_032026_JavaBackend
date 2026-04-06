package com.mockproject.notary_admin_server.dto.response;

import java.time.LocalDate;
import java.util.UUID;

import com.mockproject.notary_common.constant.CommissionStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommissionListResponse {

    private UUID id;

    private String commissionNumber;

    private String commissionState;

    private LocalDate issueDate;

    private LocalDate expirationDate;

    private CommissionStatus status;
}