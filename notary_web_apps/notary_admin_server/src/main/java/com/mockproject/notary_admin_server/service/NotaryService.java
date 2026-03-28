package com.mockproject.notary_admin_server.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.UUID;

import com.mockproject.notary_admin_server.dto.request.UpdateNotaryInfoRequest;
import com.mockproject.notary_admin_server.dto.response.NotaryAdminResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryPublicResponse;
import com.mockproject.notary_admin_server.mapper.NotaryMapper;
import com.mockproject.notary_admin_server.repository.NotaryRepository;
import com.mockproject.notary_common.constant.UserStatus;
import com.mockproject.notary_common.entity.notary.Notary;

/**
 * NotaryService
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      PhamTam      create
 */
@Service
public class NotaryService {
    private final NotaryRepository notaryRepository;
    private final NotaryMapper notaryMapper;
    public NotaryService(NotaryRepository notaryRepository, NotaryMapper notaryMapper) {
        this.notaryRepository = notaryRepository;
        this.notaryMapper = notaryMapper;
    }

    private Notary findNotary(UUID idNotary) {
        return notaryRepository.findById(idNotary)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notary not found"));
    }

    public NotaryPublicResponse getPersonalInfo(UUID idNotary) {
        Notary notary = findNotary(idNotary);
        return notaryMapper.toPublicResponse(notary);
    }

    public NotaryAdminResponse getNotaryInfoForAdmin(UUID idNotary) {
        Notary notary = findNotary(idNotary);
        return notaryMapper.toAdminResponse(notary);
    }

    private void applyCommonFields(Notary notary, UpdateNotaryInfoRequest request) {
        if (request.phone() != null) notary.setPhone(request.phone());
        if (request.photoUrl() != null) notary.setPhotoUrl(request.photoUrl());
        if (request.dateOfBirth() != null) notary.setDateOfBirth(request.dateOfBirth());
        if (request.startDate() != null) notary.setStartDate(request.startDate());
        if (request.address() != null) notary.setAddress(request.address());
        notary.setUpdatedAt(LocalDateTime.now());
    }

    public NotaryPublicResponse updatePersonalInfo(UUID idNotary, UpdateNotaryInfoRequest request) {
        Notary notary = findNotary(idNotary);
        applyCommonFields(notary, request);
        Notary saved = notaryRepository.save(notary);
        return notaryMapper.toPublicResponse(saved);
    }

    public NotaryPublicResponse updatePersonalInfoByAdmin(UUID idNotary, UpdateNotaryInfoRequest request) {
        Notary notary = findNotary(idNotary);

        applyCommonFields(notary, request);
        if (request.email() != null) notary.setEmail(request.email());
        if (request.fullName() != null) notary.setFullName(request.fullName());
        if (request.ssn() != null) notary.setSsn(request.ssn());
        if (request.status() != null) notary.setStatus(UserStatus.valueOf(request.status()));
        if (request.internalNotes() != null) notary.setInternalNotes(request.internalNotes());
        if (request.employmentType() != null) notary.setEmploymentType(request.employmentType());

        Notary saved = notaryRepository.save(notary);
        return notaryMapper.toPublicResponse(saved);
    }


}
