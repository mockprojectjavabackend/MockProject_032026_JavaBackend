package com.mockproject.notary_admin_server.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.mockproject.notary_admin_server.dto.response.NotaryDetailResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryOverviewResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryStatusResponse;
import com.mockproject.notary_admin_server.repository.NotaryOverviewRepository;
import com.mockproject.notary_admin_server.service.NotaryOverviewService;
import com.mockproject.notary_common.constant.UserStatus;
import com.mockproject.notary_common.entity.notary.*;
import org.springframework.stereotype.Service;

/**
 * NotaryOverviewServiceImpl
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 26-03-2026      DangQuoc      create
 */
@Service
public class NotaryOverviewServiceImpl implements NotaryOverviewService {
    private final NotaryOverviewRepository repo;

    public NotaryOverviewServiceImpl(NotaryOverviewRepository repo) {
        this.repo = repo;
    }

    /**
     * update notary status
     * @param notaryId
     * @return NotaryStatusResponse
     */
    @Override
    public NotaryStatusResponse deactivateNotary(UUID notaryId) {
        Optional<Notary> notaryIds = repo.findById(notaryId);
        Notary notary = notaryIds.get();
        UserStatus currentStatus = notary.getStatus();

        switch (currentStatus){
            case ACTIVE -> notary.setStatus(UserStatus.INACTIVE);
            case INACTIVE -> notary.setStatus(UserStatus.BLOCKED);
            case BLOCKED -> notary.setStatus(UserStatus.ACTIVE);
        }

        notary.setUpdatedAt(LocalDateTime.now());
        repo.save(notary);
        return mapNotaryStatus(notary);
    }

    /**
     * get notary detail
     * @param notaryId
     * @return NotaryDetailResponse
     */
    @Override
    public NotaryDetailResponse getNotaryDetail(UUID notaryId) {
        return repo.getNatoryDetails(notaryId);
    }

    /**
     * get notary overview
     * @param notaryId
     * @return
     */
    @Override
    public NotaryOverviewResponse getNotaryOverview(UUID notaryId) {
        List<Object[]> rows = repo.getNatoryOverview(notaryId);

        NotaryCommission commission = null;
        NotaryBonds bond = null;
        NotaryInsurance insurance = null;
        NotaryDocument document= null;
        Notary notary = null;
        Set<String> serviceAreas = new HashSet<>();

        // loop through joined result (can be duplicated due to multiple joins)
        for (Object[] row : rows){
            Notary n = (Notary) row[0];
            NotaryCommission c = (NotaryCommission) row[1];
            NotaryBonds b = (NotaryBonds) row[2];
            NotaryInsurance i = (NotaryInsurance) row[3];
            NotaryDocument d = (NotaryDocument) row[4];
            NotaryServiceArea sa = (NotaryServiceArea) row[5];

            // take first non-null value for each object
            if (commission == null && c != null) commission = c;
            if (bond == null && b != null) bond = b;
            if (insurance == null && i != null) insurance = i;
            if (notary == null && n != null) notary = n;
            if (document == null && d != null) document = d;

            // collect unique service areas
            if (sa != null){
                serviceAreas.add(sa.getCountyName());
            }
        }

        return new NotaryOverviewResponse(
                mapCommission(commission),
                mapBond(bond),
                mapInsurance(insurance),
                mapDocument(document),
                mapNotary(notary),
                new ArrayList<>(serviceAreas));
    }

    /**
     * map commission entity to dto
     * @param c
     * @return
     */
    private NotaryOverviewResponse.Commission mapCommission(NotaryCommission c) {
        if (c == null) return null;
        NotaryOverviewResponse.Commission dto = new NotaryOverviewResponse.Commission();
        dto.setStatus(c.getStatus().name());
        dto.setExpires(c.getExpirationDate().toString());
        return dto;
    }

    /**
     * map bond entity to dto
     * @param b
     * @return
     */
    private NotaryOverviewResponse.Bond mapBond(NotaryBonds b) {
        if (b == null) return null;
        NotaryOverviewResponse.Bond dto = new NotaryOverviewResponse.Bond();
        boolean expired = b.getExpirationDate().isBefore(LocalDate.now());
        dto.setStatus(expired ? "Expired" : "Valid");
        dto.setCoverage(b.getBondAmount());
        return dto;
    }

    /**
     * map insurance entity to dto
     * @param i
     * @return
     */
    private NotaryOverviewResponse.EoInsurance mapInsurance(NotaryInsurance i) {
        if (i == null) return null;
        NotaryOverviewResponse.EoInsurance dto = new NotaryOverviewResponse.EoInsurance();
        boolean expired = i.getExpirationDate().isBefore(LocalDate.now());
        dto.setStatus(expired ? "Expired" : "Valid");
        dto.setExpires(i.getExpirationDate().toString());
        return dto;
    }

    /**
     * map document entity to dto
     * @param d
     * @return
     */
    private NotaryOverviewResponse.Document mapDocument(NotaryDocument d) {
        if (d == null) return null;
        NotaryOverviewResponse.Document dto = new NotaryOverviewResponse.Document();
        dto.setDocCategory(d.getDocCategory());
        dto.setVerifiedStatus(d.getVerifiedStatus());
        dto.setUploadDate(d.getUploadDate());
        return dto;
    }

    /**
     * map notary entity to contact information dto
     * @param n
     * @return
     */
    private NotaryOverviewResponse.ContactInpormation mapNotary(Notary n) {
        if (n == null) return null;
        NotaryOverviewResponse.ContactInpormation dto = new NotaryOverviewResponse.ContactInpormation();
        dto.setEmail(n.getEmail());
        dto.setPhone(n.getPhone());
        dto.setAddress(n.getAddress());
        return dto;
    }

    /**
     * map notary entity to status response
     * @param n
     * @return
     */
    private NotaryStatusResponse mapNotaryStatus(Notary n) {
        if (n == null) return null;
        NotaryStatusResponse dto = new NotaryStatusResponse();
        dto.setId(n.getId());
        dto.setStatus(n.getStatus());
        dto.setUpdatedAt(n.getUpdatedAt());
        return dto;
    }
}
