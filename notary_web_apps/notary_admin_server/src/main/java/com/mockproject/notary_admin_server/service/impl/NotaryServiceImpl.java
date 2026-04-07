package com.mockproject.notary_admin_server.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.mockproject.notary_admin_server.configuration.security.SecurityUtils;
import com.mockproject.notary_admin_server.service.UploadFileService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.mockproject.notary_admin_server.service.INotaryService;
import com.mockproject.notary_admin_server.service.NotaryServiceAreaService;
import com.mockproject.notary_admin_server.service.StateService;

import com.mockproject.notary_admin_server.dto.request.UpdateNotaryInfoRequest;
import com.mockproject.notary_admin_server.dto.response.StateResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryBaseResponse;

import com.mockproject.notary_admin_server.mapper.NotaryMapper;
import com.mockproject.notary_admin_server.repository.NotaryRepository;
import com.mockproject.notary_common.entity.notary.Notary;


/**
 * NotaryService
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      PhamTam      create
 * 02-04-2026      PhamTam      edit
 */
@Service
public class NotaryServiceImpl implements INotaryService {
    private final NotaryRepository notaryRepository;
    private final NotaryMapper notaryMapper;
    private final NotaryServiceAreaService notaryServiceAreaService;
    private final StateService stateService;
    private final UploadFileService uploadFileService;

    public NotaryServiceImpl(NotaryRepository notaryRepository, NotaryMapper notaryMapper, NotaryServiceAreaServiceImpl notaryServiceAreaService, StateServiceImpl stateService, UploadFileService uploadFileService) {
        this.notaryRepository = notaryRepository;
        this.notaryMapper = notaryMapper;
        this.notaryServiceAreaService = notaryServiceAreaService;
        this.stateService = stateService;
        this.uploadFileService = uploadFileService;
    }

    private Notary findNotary(UUID idNotary) {
        return notaryRepository.findById(idNotary)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notary not found"));
    }

    @Override
    public NotaryBaseResponse getPersonalInfo(UUID idNotary) {
        boolean isAdmin = SecurityUtils.isAdmin();
        return getNotaryResponse(idNotary, isAdmin);
    }

    /**
     * Update notary info. Admin can update all fields, public can only update common fields.
     *
     * @param idNotary notary UUID
     * @param request  update request
     */
    @Transactional
    @Override
    public NotaryBaseResponse updatePersonalInfo(UUID idNotary, UpdateNotaryInfoRequest request) {

        boolean isAdmin = SecurityUtils.isAdmin();

        Notary notary = findNotary(idNotary);

        applyCommonFields(notary, request);

        if (isAdmin) {
            applyAdminFields(notary, request);
        }

        notary.setUpdatedAt(LocalDateTime.now());
        Notary saved = notaryRepository.save(notary);
        List<StateResponse> states = stateService.getAllStatesByNotary(idNotary);

        return isAdmin
                ? notaryMapper.toAdminResponse(saved, states)
                : notaryMapper.toPublicResponse(saved, states);
    }

    // Fields any user can update
    private void applyCommonFields(Notary notary, UpdateNotaryInfoRequest request) {

        try {
            if (request.getProfilePhoto() != null)
            {
                String photoUrl = uploadFileService.uploadFile(request.getProfilePhoto(),"notary");
                notary.setPhotoUrl(photoUrl);
            }

            if (request.getAddress() != null)   notary.setAddress(request.getAddress());
            if (request.getCity() != null)      notary.setCity(request.getCity());
            if (request.getZipCode() != null)   notary.setZipCode(request.getZipCode());
            if (request.getStates() != null)    notaryServiceAreaService.updateStates(notary.getId(), request.getStates());

        }
        catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    // Fields only admin can update
    private void applyAdminFields(Notary notary, UpdateNotaryInfoRequest request) {
        if (request.getPhone() != null)          notary.setPhone(request.getPhone());
        if (request.getDateOfBirth() != null)    notary.setDateOfBirth(request.getDateOfBirth());
        if (request.getStartDate() != null)      notary.setStartDate(request.getStartDate());
        if (request.getFullName() != null)       notary.setFullName(request.getFullName());
        if (request.getSsn() != null)            notary.setSsn(request.getSsn());
        if (request.getInternalNotes() != null)  notary.setInternalNotes(request.getInternalNotes());
        if (request.getEmploymentType() != null) notary.setEmploymentType(request.getEmploymentType());
        if (request.getEmail() !=null){
            notary.getUser().setEmail(request.getEmail());
        }
    }

    // Return admin or public response based on role
    private NotaryBaseResponse getNotaryResponse(UUID idNotary, boolean isAdmin) {
        Notary notary = findNotary(idNotary);
        List<StateResponse> states = stateService.getAllStatesByNotary(idNotary);

        return isAdmin
                ? notaryMapper.toAdminResponse(notary, states)  // NotaryAdminResponse
                : notaryMapper.toPublicResponse(notary, states); // NotaryPublicResponse
    }


}
