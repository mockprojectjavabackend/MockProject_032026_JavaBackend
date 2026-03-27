package com.mockproject.notary_admin_server.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.mockproject.notary_admin_server.repository.NotaryBondsRepository;
import com.mockproject.notary_common.entity.notary.Notary;
import com.mockproject.notary_common.entity.notary.NotaryBonds;

@Service
public class NotaryBondsService {
    private final NotaryBondsRepository notaryBondsRepository;
    private final EntityManager entityManager;

    public NotaryBondsService(NotaryBondsRepository notaryBondsRepository, EntityManager entityManager) {
        this.notaryBondsRepository = notaryBondsRepository;
        this.entityManager = entityManager;
    }

    public Optional<NotaryBonds> getBondByNotaryId(UUID notaryId) {
        return notaryBondsRepository.findFirstByNotary_IdOrderByCreatedAtDesc(notaryId);
    }

    public java.util.List<NotaryBonds> getAllBondsByNotaryId(UUID notaryId) {
        return notaryBondsRepository.findAllByNotary_IdOrderByCreatedAtDesc(notaryId);
    }

    public NotaryBonds createBond(UUID notaryId, String providerName, BigDecimal bondAmount,
            LocalDate effectiveDate, LocalDate expirationDate, String fileUrl) {
        Notary notary = entityManager.find(Notary.class, notaryId);
        if (notary == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notary not found");
        }

        NotaryBonds bond = NotaryBonds.builder()
                .notary(notary)
                .providerName(providerName)
                .bondAmount(bondAmount)
                .effectiveDate(effectiveDate)
                .expirationDate(expirationDate)
                .fileUrl(fileUrl)
                .build();

        return notaryBondsRepository.save(bond);
    }

    public NotaryBonds updateBond(UUID notaryId, String providerName, BigDecimal bondAmount,
            LocalDate effectiveDate, LocalDate expirationDate, String fileUrl) {
        NotaryBonds bond = notaryBondsRepository.findFirstByNotary_IdOrderByCreatedAtDesc(notaryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notary bond not found"));

        bond.setProviderName(providerName);
        bond.setBondAmount(bondAmount);
        bond.setEffectiveDate(effectiveDate);
        bond.setExpirationDate(expirationDate);
        if (fileUrl != null) {
            bond.setFileUrl(fileUrl);
        }

        return notaryBondsRepository.save(bond);
    }

    public NotaryBonds uploadBondFile(UUID notaryId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is required");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File name is required");
        }

        NotaryBonds bond = notaryBondsRepository.findFirstByNotary_IdOrderByCreatedAtDesc(notaryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notary bond not found"));

        String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
        bond.setFileUrl("https://files/" + sanitizedFilename);

        return notaryBondsRepository.save(bond);
    }

}
