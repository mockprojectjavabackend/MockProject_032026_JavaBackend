package com.mockproject.notary_admin_server.controller;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.request.Capability.CapabilityRequest;
import com.mockproject.notary_admin_server.dto.response.capability.ServiceCapabilityResponse;
import com.mockproject.notary_admin_server.service.ICapabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * CapabilityController
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      ThoHa       create
 */

@RestController
@RequestMapping("/api/notaries")
@RequiredArgsConstructor
public class CapabilityController {
    private final ICapabilityService capabilityService;

    @GetMapping("/{notaryId}/service_capability")
    public ResponseEntity<ApiSuccessResponse<ServiceCapabilityResponse>> getCapability(@PathVariable UUID notaryId) {
        return ResponseEntity.ok(ApiSuccessResponse.ok(capabilityService.getCapability(notaryId)));
    }

    @PostMapping("/{notaryId}/service_capability")
    public ResponseEntity<ApiSuccessResponse<ServiceCapabilityResponse>> createCapability(@PathVariable UUID notaryId,
                                                                          @Valid @RequestBody CapabilityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiSuccessResponse.created(capabilityService.createCapability(notaryId, request)));
    }

    @PutMapping("/{notaryId}/service_capability")
    public ResponseEntity<ApiSuccessResponse<ServiceCapabilityResponse>> updateCapability(@PathVariable UUID notaryId,
                                                                          @Valid @RequestBody CapabilityRequest request) {
        return ResponseEntity.ok(ApiSuccessResponse.ok(capabilityService.updateCapability(notaryId, request)));
    }
}
