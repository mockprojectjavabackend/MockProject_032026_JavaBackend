package com.mockproject.notary_admin_server.controller;

import com.mockproject.notary_admin_server.dto.ApiResponse;
import com.mockproject.notary_admin_server.dto.request.Capability.CreateCapabilityRequest;
import com.mockproject.notary_admin_server.dto.response.CapabilityResponse;
import com.mockproject.notary_admin_server.dto.response.ServiceCapabilityResponse;
import com.mockproject.notary_admin_server.service.ICapabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notaries")
@RequiredArgsConstructor
public class CapabilityController {
    private final ICapabilityService capabilityService;

    @GetMapping("/{id}/service_capability")
    public ApiResponse<CapabilityResponse> getCapability(@PathVariable UUID id) {
        return ApiResponse.success(capabilityService.getCapability(id));
    }

    @PostMapping("/{id}/service_capability")
    public ApiResponse<ServiceCapabilityResponse> createCapability(@PathVariable UUID id,
                                                                   @RequestBody @Valid CreateCapabilityRequest request) {
        return ApiResponse.success(capabilityService.createCapability(id, request));
    }
}
