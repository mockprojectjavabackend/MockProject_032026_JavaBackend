package com.mockproject.notary_admin_server.service.impl;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.stereotype.Service;

import com.mockproject.notary_admin_server.dto.response.NotaryDetailResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryOverviewDTO;
import com.mockproject.notary_admin_server.dto.response.NotaryOverviewResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryStatusResponse;
import com.mockproject.notary_admin_server.exception.AppException;
import com.mockproject.notary_admin_server.exception.errorCode.BaseErrorCode;
import com.mockproject.notary_admin_server.repository.NotaryOverviewRepository;
import com.mockproject.notary_admin_server.repository.NotaryServiceAreaRepository;
import com.mockproject.notary_admin_server.service.NotaryOverviewService;
import com.mockproject.notary_common.constant.UserStatus;
import com.mockproject.notary_common.entity.notary.*;

/**
 * NotaryOverviewServiceImpl
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 26-03-2026      DangQuoc      create
 * 28-03-2026      DangQuoc      edit getNotaryOverview
 * 28-03-2026      DangQuoc      edit deactivateNotary
 */
@Service
public class NotaryOverviewServiceImpl implements NotaryOverviewService {
    private final NotaryOverviewRepository notaryOverviewRepository;
    private final NotaryServiceAreaRepository notaryServiceAreaRepository;

    public NotaryOverviewServiceImpl(NotaryOverviewRepository notaryOverviewRepository, NotaryServiceAreaRepository notaryServiceAreaRepository) {
        this.notaryOverviewRepository = notaryOverviewRepository;
        this.notaryServiceAreaRepository = notaryServiceAreaRepository;
    }

    /**
     * update notary status
     * @param notaryId
     * @return NotaryStatusResponse
     */
    @Override
    public NotaryStatusResponse deactivateNotary(UUID notaryId) {
        Notary notary = notaryOverviewRepository.findById(notaryId)
                .orElseThrow(() -> new AppException(BaseErrorCode.NOTARY_NOT_FOUND));

        UserStatus currentStatus = notary.getStatus();
        switch (currentStatus) {
            case ACTIVE -> notary.setStatus(UserStatus.INACTIVE);
            case INACTIVE -> notary.setStatus(UserStatus.BLOCKED);
            case BLOCKED -> notary.setStatus(UserStatus.ACTIVE);
        }

        notary.setUpdatedAt(LocalDateTime.now());
        notaryOverviewRepository.save(notary);
        return mapToNotaryStatusResponse(notary);
    }

    /**
     * get notary detail
     * @param notaryId
     * @return NotaryDetailResponse
     */
    @Override
    public NotaryDetailResponse getNotaryDetail(UUID notaryId) {
        NotaryDetailResponse response = notaryOverviewRepository.getNatoryDetails(notaryId);

        if (response == null) {
            throw new AppException(BaseErrorCode.NOTARY_NOT_FOUND);
        }

        return response;
    }

    /**
     * get notary overview
     * @param notaryId
     * @return
     */
    @Override
    public NotaryOverviewResponse getNotaryOverview(UUID notaryId) {

        NotaryOverviewDTO nod = notaryOverviewRepository.getOverview(notaryId)
                .orElseThrow(() -> new AppException(BaseErrorCode.NOTARY_NOT_FOUND));

        List<String> areas = notaryServiceAreaRepository.getServiceAreas(notaryId);

        return mapToNotaryOverviewResponse(nod, areas);
    }

    /**
     * Map Notary Overview DTO to Response
     * @param nod, areas
     * @return
     */
    private NotaryOverviewResponse mapToNotaryOverviewResponse(NotaryOverviewDTO nod, List<String> areas) {
        return new NotaryOverviewResponse(
                mapToCommission(nod),
                mapToBond(nod),
                mapToInsurance(nod),
                mapToDocument(nod),
                mapToContactInformation(nod),
                areas
        );
    }

    /**
     * Map Commission DTO to Response
     * @param nod
     * @return
     */
    private NotaryOverviewResponse.Commission mapToCommission(NotaryOverviewDTO nod) {
        return new NotaryOverviewResponse.Commission(
                nod.status().name(),
                nod.expirationDate(),
                nod.expectedRenewalDate(),
                nod.isRenewalApplied()
        );
    }

    /**
     * Map Bond DTO to Response
     * @param nod
     * @return
     */
    private NotaryOverviewResponse.Bond mapToBond(NotaryOverviewDTO nod) {
        return new NotaryOverviewResponse.Bond(
                nod.bondStatus(),
                nod.bondAmount()
        );
    }

    /**
     * Map Insurance DTO to Response
     * @param nod
     * @return
     */
    private NotaryOverviewResponse.EoInsurance mapToInsurance(NotaryOverviewDTO nod) {
        return new NotaryOverviewResponse.EoInsurance(
                nod.insuranceStatus(),
                nod.insuranceExpirationDate(),
                nod.insuranceEffectiveDate()
        );
    }

    /**
     * Map Document DTO to Response
     * @param nod
     * @return
     */
    private NotaryOverviewResponse.Document mapToDocument(NotaryOverviewDTO nod) {
        return new NotaryOverviewResponse.Document(
                nod.docCategory(),
                nod.verifiedStatus(),
                nod.uploadDate()
        );
    }

    /**
     * Map Contact Information DTO to Response
     * @param nod
     * @return
     */
    private NotaryOverviewResponse.ContactInformation mapToContactInformation(NotaryOverviewDTO nod) {
        return new NotaryOverviewResponse.ContactInformation(
                nod.email(),
                nod.phone(),
                nod.address()
        );
    }

    /**
     * Map notary entity to status response
     * @param n
     * @return
     */
    private NotaryStatusResponse mapToNotaryStatusResponse(Notary n) {
        return n == null ? null : new NotaryStatusResponse(
                n.getId(),
                n.getStatus(),
                n.getUpdatedAt()
        );
    }
}
