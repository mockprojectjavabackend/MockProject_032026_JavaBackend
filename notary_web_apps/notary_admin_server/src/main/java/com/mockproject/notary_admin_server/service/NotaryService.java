package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_admin_server.dto.response.StateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private final NotaryServiceAreaService notaryServiceAreaService;
    private final StateService stateService;
    public NotaryService(NotaryRepository notaryRepository, NotaryMapper notaryMapper, NotaryServiceAreaService notaryServiceAreaService, StateService stateService) {
        this.notaryRepository = notaryRepository;
        this.notaryMapper = notaryMapper;
        this.notaryServiceAreaService = notaryServiceAreaService;
        this.stateService = stateService;
    }

    private Notary findNotary(UUID idNotary) {
        return notaryRepository.findById(idNotary)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notary not found"));
    }

    public NotaryPublicResponse getPersonalInfo(UUID idNotary) {
        Notary notary = findNotary(idNotary);
        List<StateResponse> states = stateService.getAllStateByNotary(idNotary);
        return notaryMapper.toPublicResponse(notary,states);
    }

    public NotaryAdminResponse getNotaryInfoForAdmin(UUID idNotary) {
        Notary notary = findNotary(idNotary);
        List<StateResponse> states = stateService.getAllStateByNotary(idNotary);
        return notaryMapper.toAdminResponse(notary,states);
    }

    private void applyCommonFields(Notary notary, UpdateNotaryInfoRequest request) {
        if (request.getPhotoUrl() != null) notary.setPhotoUrl(request.getPhotoUrl());
        if (request.getAddress() != null) notary.setAddress(request.getAddress());
        if (request.getCity()!= null) notary.setCity(request.getCity());
        if (request.getZipCode()!= null) notary.setZipCode(request.getZipCode());
        if (request.getStates()!=null) notaryServiceAreaService.updateStates(notary.getId(),request.getStates());

        notary.setUpdatedAt(LocalDateTime.now());
    }

    public NotaryPublicResponse updatePersonalInfo(UUID idNotary, UpdateNotaryInfoRequest request) {
        Notary notary = findNotary(idNotary);
        applyCommonFields(notary, request);
        Notary saved = notaryRepository.save(notary);
        List<StateResponse> states = stateService.getAllStateByNotary(idNotary);
        return notaryMapper.toPublicResponse(saved,states);
    }

    public NotaryPublicResponse updatePersonalInfoByAdmin(UUID idNotary, UpdateNotaryInfoRequest request) {
        Notary notary = findNotary(idNotary);

        applyCommonFields(notary, request);
        if (request.getPhone() != null) notary.setPhone(request.getPhone());
        if (request.getDateOfBirth() != null) notary.setDateOfBirth(request.getDateOfBirth() );
        if (request.getStartDate() != null) notary.setStartDate(request.getStartDate());
        if (request.getEmail() != null) notary.setEmail(request.getEmail());
        if (request.getFullName()!= null) notary.setFullName(request.getFullName());
        if (request.getSsn() != null) notary.setSsn(request.getSsn());
        if (request.getStatus()!= null) notary.setStatus(request.getStatus());
        if (request.getInternalNotes() != null) notary.setInternalNotes(request.getInternalNotes());
        if (request.getEmploymentType() != null) notary.setEmploymentType(request.getEmploymentType());

        Notary saved = notaryRepository.save(notary);
        List<StateResponse> states = stateService.getAllStateByNotary(idNotary);
        return notaryMapper.toPublicResponse(saved,states);
    }


}
