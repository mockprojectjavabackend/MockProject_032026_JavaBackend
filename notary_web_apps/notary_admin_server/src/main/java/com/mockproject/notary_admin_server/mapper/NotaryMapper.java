package com.mockproject.notary_admin_server.mapper;

import java.util.List;
import org.springframework.stereotype.Component;

import com.mockproject.notary_admin_server.dto.response.StateResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryAdminResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryPublicResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryBaseResponse;
import com.mockproject.notary_common.entity.notary.Notary;


/**
 * NotaryMapper
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      PhamTam      create
 * 02-04-2026      PhamTam      edit
 */

@Component
public class NotaryMapper {

    // Map shared fields to any NotaryBaseResponse builder
    private <T extends NotaryBaseResponse.NotaryBaseResponseBuilder<?, ?>> T mapCommonFields(
            T builder, Notary notary, List<StateResponse> states) {
        builder.id(notary.getId())
                .fullName(notary.getFullName())
                .phone(notary.getPhone())
                .photoUrl(notary.getPhotoUrl())
                .dateOfBirth(notary.getDateOfBirth())
                .startDate(notary.getStartDate())
                .userId(notary.getUser().getId())
                .email(notary.getUser().getEmail())
                .createdAt(notary.getCreatedAt())
                .updatedAt(notary.getUpdatedAt())
                .address(notary.getAddress())
                .city(notary.getCity())
                .zipCode(notary.getZipCode())
                .states(states);
        return builder;
    }

    public NotaryPublicResponse toPublicResponse(Notary notary, List<StateResponse> states) {
        return mapCommonFields(NotaryPublicResponse.builder(), notary, states).build();
    }

    public NotaryAdminResponse toAdminResponse(Notary notary, List<StateResponse> states) {
        return mapCommonFields(NotaryAdminResponse.builder(), notary, states)
                .ssn(notary.getSsn())
                .employmentType(notary.getEmploymentType())
                .internalNotes(notary.getInternalNotes())
                .build();
    }

}
