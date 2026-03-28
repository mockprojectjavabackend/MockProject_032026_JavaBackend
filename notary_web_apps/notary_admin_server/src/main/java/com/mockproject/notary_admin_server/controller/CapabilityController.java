package com.mockproject.notary_admin_server.controller;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.request.Capability.CapabilityRequest;
import com.mockproject.notary_admin_server.dto.response.CapabilityResponse;
import com.mockproject.notary_admin_server.dto.response.ServiceCapabilityResponse;
import com.mockproject.notary_admin_server.service.ICapabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notaries")
@RequiredArgsConstructor
public class CapabilityController {
    private final ICapabilityService capabilityService;

    @GetMapping("/{id}/service_capability")
    public ApiSuccessResponse<CapabilityResponse> getCapability(@PathVariable UUID id) {
        return ApiSuccessResponse.ok(capabilityService.getCapability(id));
    }

    @PostMapping("/{id}/service_capability")
    public ApiSuccessResponse<ServiceCapabilityResponse> createCapability(@PathVariable UUID id,
                                                                          @Valid @RequestBody CapabilityRequest request) {
        return ApiSuccessResponse.created(capabilityService.createCapability(id, request));
    }

    @PutMapping("/{id}/service_capability")
    public ApiSuccessResponse<ServiceCapabilityResponse> updateCapability(@PathVariable UUID id,
                                                                          @Valid @RequestBody CapabilityRequest request) {
        return ApiSuccessResponse.ok(capabilityService.updateCapability(id, request));
    }
}
