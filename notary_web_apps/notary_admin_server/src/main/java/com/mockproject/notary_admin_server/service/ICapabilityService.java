package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_admin_server.dto.request.Capability.CapabilityRequest;
import com.mockproject.notary_admin_server.dto.response.CapabilityResponse;
import com.mockproject.notary_admin_server.dto.response.ServiceCapabilityResponse;

import java.util.UUID;

public interface ICapabilityService {
    CapabilityResponse getCapability(UUID notaryId);
    ServiceCapabilityResponse createCapability(UUID notaryId, CapabilityRequest createCapabilityRequest);
    ServiceCapabilityResponse updateCapability(UUID notaryId, CapabilityRequest createCapabilityRequest);
}
