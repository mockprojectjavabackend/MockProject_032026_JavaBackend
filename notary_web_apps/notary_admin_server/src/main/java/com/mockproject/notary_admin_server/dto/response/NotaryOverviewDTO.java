package com.mockproject.notary_admin_server.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.mockproject.notary_common.constant.CommissionStatus;
import com.mockproject.notary_common.constant.DocCategory;
import com.mockproject.notary_common.constant.VerifiedStatus;

/**
 * NotaryOverviewDTO
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 28-03-2026      DangQuoc      create
 */
public record NotaryOverviewDTO (
        //Commission
        CommissionStatus status,
        LocalDate expirationDate,
        LocalDate expectedRenewalDate,
        Boolean isRenewalApplied,

        //Bond
        String bondStatus,
        BigDecimal bondAmount,

        //Insurance
        String insuranceStatus,
        LocalDate insuranceExpirationDate,
        LocalDate insuranceEffectiveDate,

        //Document
        DocCategory docCategory,
        VerifiedStatus verifiedStatus,
        LocalDateTime uploadDate,

        //Notary Information
        String email,
        String phone,
        String address
){}

