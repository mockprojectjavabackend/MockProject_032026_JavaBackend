package com.mockproject.notary_admin_server.service;


import java.util.UUID;
import jakarta.transaction.Transactional;

import com.mockproject.notary_admin_server.dto.request.UpdateNotaryInfoRequest;
import com.mockproject.notary_admin_server.dto.response.NotaryBaseResponse;

/**
 * NotaryService
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 02-04-2026      PhamTam      create
 */

public interface INotaryService {


    NotaryBaseResponse getPersonalInfo(UUID idNotary);

    @Transactional
    NotaryBaseResponse updatePersonalInfo(UUID idNotary, UpdateNotaryInfoRequest request);


}
