package com.mockproject.notary_admin_server.service;

import java.util.List;
import java.util.UUID;

import com.mockproject.notary_admin_server.dto.response.StateResponse;

/**
 * StateService
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 02-04-2026      PhamTam      create
 */
public interface StateService {

    List<StateResponse> getAllStates();

    List<StateResponse> getAllStatesByNotary(UUID notaryId);
}
