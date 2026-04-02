package com.mockproject.notary_admin_server.service;

import java.util.List;
import java.util.UUID;

/**
 * NotaryServiceAreaService
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 02-04-2026      PhamTam      create
 */
public interface NotaryServiceAreaService {

    void updateStates(UUID notaryId, List<UUID> newStateIds);

}
