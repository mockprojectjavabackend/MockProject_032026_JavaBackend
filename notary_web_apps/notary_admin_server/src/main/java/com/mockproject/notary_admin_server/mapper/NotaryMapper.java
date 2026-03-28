package com.mockproject.notary_admin_server.mapper;

import com.mockproject.notary_admin_server.dto.response.NotaryAdminResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryPublicResponse;
import com.mockproject.notary_common.entity.notary.Notary;
import org.springframework.stereotype.Component;

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

    public NotaryPublicResponse toPublicResponse(Notary notary) {

        return new NotaryPublicResponse(
                notary.getId(),
                notary.getFullName(),
                notary.getEmail(),
                notary.getPhone(),
                notary.getStatus(),
                notary.getPhotoUrl(),
                notary.getDateOfBirth(),
                notary.getStartDate(),
                notary.getUser().getId(),
                notary.getCreatedAt(),
                notary.getUpdatedAt(),
                notary.getAddress()
        );
    }

    public NotaryAdminResponse toAdminResponse(Notary notary) {

        return new NotaryAdminResponse(
                notary.getId(),
                notary.getFullName(),
                notary.getEmail(),
                notary.getPhone(),
                notary.getSsn(),
                notary.getStatus(),
                notary.getPhotoUrl(),
                notary.getDateOfBirth(),
                notary.getStartDate(),
                notary.getUser().getId(),
                notary.getEmploymentType(),
                notary.getInternalNotes(),
                notary.getCreatedAt(),
                notary.getUpdatedAt(),
                notary.getAddress()

        );
    }
}
