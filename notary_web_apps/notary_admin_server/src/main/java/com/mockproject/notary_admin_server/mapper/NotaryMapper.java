package com.mockproject.notary_admin_server.mapper;

import com.mockproject.notary_admin_server.dto.response.StateResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryAdminResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryPublicResponse;
import com.mockproject.notary_common.entity.notary.Notary;
import com.mockproject.notary_common.entity.notary.NotaryServiceArea;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * NotaryMapper
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      PhamTam      create
 */

@Component
public class NotaryMapper {

    public NotaryPublicResponse toPublicResponse(Notary notary, List<StateResponse> states) {
        return NotaryPublicResponse.builder()
                .id(notary.getId())
                .fullName(notary.getFullName())
                .email(notary.getEmail())
                .phone(notary.getPhone())
                .status(notary.getStatus())
                .photoUrl(notary.getPhotoUrl())
                .dateOfBirth(notary.getDateOfBirth())
                .startDate(notary.getStartDate())
                .userId(notary.getUser().getId())
                .createdAt(notary.getCreatedAt())
                .updatedAt(notary.getUpdatedAt())
                .address(notary.getAddress())
                .city(notary.getCity())
                .zipCode(notary.getZipCode())
                .states(states)
                .build();
    }

    public NotaryAdminResponse toAdminResponse(Notary notary, List<StateResponse> states) {
        return NotaryAdminResponse.builder()
                .id(notary.getId())
                .fullName(notary.getFullName())
                .email(notary.getEmail())
                .phone(notary.getPhone())
                .ssn(notary.getSsn())
                .status(notary.getStatus())
                .photoUrl(notary.getPhotoUrl())
                .dateOfBirth(notary.getDateOfBirth())
                .startDate(notary.getStartDate())
                .userId(notary.getUser().getId())
                .employmentType(notary.getEmploymentType())
                .internalNotes(notary.getInternalNotes())
                .createdAt(notary.getCreatedAt())
                .updatedAt(notary.getUpdatedAt())
                .address(notary.getAddress())
                .city(notary.getCity())
                .zipCode(notary.getZipCode())
                .states(states)
                .build();
    }

}
