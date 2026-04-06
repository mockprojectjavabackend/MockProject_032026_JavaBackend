package com.mockproject.notary_admin_server.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.mockproject.notary_common.entity.notary.NotaryBonds;

public interface NotaryBondsService {
    Optional<NotaryBonds> getBondByNotaryId(UUID notaryId);

    List<NotaryBonds> getAllBondsByNotaryId(UUID notaryId);

    NotaryBonds createBond(UUID notaryId, String providerName, BigDecimal bondAmount,
            LocalDate effectiveDate, LocalDate expirationDate, String fileUrl);

    NotaryBonds updateBond(UUID notaryId, String providerName, BigDecimal bondAmount,
            LocalDate effectiveDate, LocalDate expirationDate, String fileUrl);

    NotaryBonds uploadBondFile(UUID notaryId, MultipartFile file);
}
