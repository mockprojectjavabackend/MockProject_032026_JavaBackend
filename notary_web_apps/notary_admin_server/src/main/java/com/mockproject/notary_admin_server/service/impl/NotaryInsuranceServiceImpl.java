package com.mockproject.notary_admin_server.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.mockproject.notary_admin_server.repository.NotaryInsuranceRepository;
import com.mockproject.notary_admin_server.service.NotaryInsuranceService;
import com.mockproject.notary_common.entity.notary.Notary;
import com.mockproject.notary_common.entity.notary.NotaryInsurance;

@Service
public class NotaryInsuranceServiceImpl implements NotaryInsuranceService {

    private static final String DEFAULT_FILE_URL = "https://files/bond.pdf";

    private final NotaryInsuranceRepository notaryInsuranceRepository;
    private final EntityManager entityManager;

    public NotaryInsuranceServiceImpl(NotaryInsuranceRepository notaryInsuranceRepository,
            EntityManager entityManager) {
        this.notaryInsuranceRepository = notaryInsuranceRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<NotaryInsurance> getInsuranceByNotaryId(UUID notaryId) {
        return notaryInsuranceRepository.findFirstByNotary_IdOrderByCreatedAtDesc(notaryId);
    }

    @Override
    public List<NotaryInsurance> getAllInsurancesByNotaryId(UUID notaryId) {
        return notaryInsuranceRepository.findAllByNotary_IdOrderByCreatedAtDesc(notaryId);
    }

    @Override
    public NotaryInsurance createInsurance(UUID notaryId, String providerName, String policyNumber,
            BigDecimal coverageAmount, LocalDate expirationDate) {
        Notary notary = entityManager.find(Notary.class, notaryId);
        if (notary == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notary not found");
        }

        LocalDate effectiveDate = notary.getStartDate() != null ? notary.getStartDate() : LocalDate.now();

        NotaryInsurance insurance = NotaryInsurance.builder()
                .notary(notary)
                .providerName(providerName)
                .policyNumber(policyNumber)
                .coverageAmount(coverageAmount)
                .effectiveDate(effectiveDate)
                .expirationDate(expirationDate)
                .fileUrl(DEFAULT_FILE_URL)
                .build();

        return notaryInsuranceRepository.save(insurance);
    }

    @Override
    public NotaryInsurance updateInsurance(UUID notaryId, String providerName, String policyNumber,
            BigDecimal coverageAmount, LocalDate expirationDate) {
        NotaryInsurance insurance = notaryInsuranceRepository.findFirstByNotary_IdOrderByCreatedAtDesc(notaryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notary insurance not found"));

        insurance.setProviderName(providerName);
        insurance.setPolicyNumber(policyNumber);
        insurance.setCoverageAmount(coverageAmount);
        insurance.setExpirationDate(expirationDate);

        return notaryInsuranceRepository.save(insurance);
    }

    @Override
    public NotaryInsurance uploadInsuranceFile(UUID notaryId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is required");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File name is required");
        }

        NotaryInsurance insurance = notaryInsuranceRepository.findFirstByNotary_IdOrderByCreatedAtDesc(notaryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notary insurance not found"));

        String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
        insurance.setFileUrl("https://files/" + sanitizedFilename);

        return notaryInsuranceRepository.save(insurance);
    }
}