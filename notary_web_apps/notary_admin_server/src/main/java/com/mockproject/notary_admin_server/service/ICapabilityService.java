package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_admin_server.dto.request.Capability.CreateCapabilityRequest;
import com.mockproject.notary_admin_server.dto.response.CapabilityResponse;
import com.mockproject.notary_admin_server.dto.response.ServiceCapabilityResponse;

import java.util.UUID;

public interface ICapabilityService {
    CapabilityResponse getCapability(UUID notaryId);
    ServiceCapabilityResponse createCapability(UUID noUuid, CreateCapabilityRequest createCapabilityRequest);
}
