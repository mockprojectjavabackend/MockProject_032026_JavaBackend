package com.mockproject.notary_admin_server.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.mockproject.notary_common.entity.notary.NotaryInsurance;

public interface NotaryInsuranceService {

    Optional<NotaryInsurance> getInsuranceByNotaryId(UUID notaryId);

    List<NotaryInsurance> getAllInsurancesByNotaryId(UUID notaryId);

    NotaryInsurance createInsurance(UUID notaryId, String providerName, String policyNumber,
            BigDecimal coverageAmount, LocalDate expirationDate);

    NotaryInsurance updateInsurance(UUID notaryId, String providerName, String policyNumber,
            BigDecimal coverageAmount, LocalDate expirationDate);

    NotaryInsurance uploadInsuranceFile(UUID notaryId, MultipartFile file);
}