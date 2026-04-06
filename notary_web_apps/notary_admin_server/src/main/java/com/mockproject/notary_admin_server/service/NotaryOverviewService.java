package com.mockproject.notary_admin_server.service;

import java.util.UUID;

import com.mockproject.notary_admin_server.dto.response.NotaryDetailResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryOverviewResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryStatusResponse;

/**
 * NotaryOverviewService
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 26-03-2026      DangQuoc      create
 */
public interface NotaryOverviewService {
    public NotaryStatusResponse deactivateNotary(UUID notaryId);
    public NotaryDetailResponse getNotaryDetail(UUID notaryId);
    public NotaryOverviewResponse getNotaryOverview(UUID notaryId);
}
