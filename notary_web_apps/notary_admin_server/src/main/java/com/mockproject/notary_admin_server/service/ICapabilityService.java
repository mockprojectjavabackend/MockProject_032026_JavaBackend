package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_admin_server.dto.request.Capability.CapabilityRequest;
import com.mockproject.notary_admin_server.dto.response.capability.CapabilityResponse;
import com.mockproject.notary_admin_server.dto.response.capability.ServiceCapabilityResponse;

import java.util.UUID;

/**
 * ICapabilityService
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      ThoHa       create
 */

public interface ICapabilityService {
    CapabilityResponse getCapability(UUID notaryId);
    ServiceCapabilityResponse createCapability(UUID notaryId, CapabilityRequest createCapabilityRequest);
    ServiceCapabilityResponse updateCapability(UUID notaryId, CapabilityRequest updateCapabilityRequest);
}
