package com.mockproject.notary_admin_server.controller;

import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.request.UpdateNotaryInfoRequest;
import com.mockproject.notary_admin_server.service.impl.NotaryServiceImpl;
import com.mockproject.notary_admin_server.dto.request.NotaryCreateRequestDTO;
import com.mockproject.notary_admin_server.dto.request.NotaryUpdateRequestDTO;
import com.mockproject.notary_admin_server.dto.response.*;
import com.mockproject.notary_admin_server.service.NotaryService;



/**
 * NotaryController
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------

 * 27-03-2026      PhamTam      create
 * 27-03-2026      TranMinh    create
 */

@RestController
@Validated
@RequestMapping()
@RequiredArgsConstructor
public class NotaryController {
    private final NotaryService notaryService;
    private final NotaryServiceImpl notaryServiceImpl;

    @GetMapping("/notaries")
    public ResponseEntity<ApiSuccessResponse<PagedResponse<NotaryResponseDTO>>> getNotaryList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String service_type,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {

        ApiSuccessResponse<PagedResponse<NotaryResponseDTO>> response = notaryService.getNotaries(
                status, state, service_type, search, page, limit
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/notaries/{notary_id}")
    public ResponseEntity<ApiSuccessResponse<NotaryDetailResponseDTO>> getNotaryDetail(
            @PathVariable("notary_id") UUID notaryId) {
        ApiSuccessResponse<NotaryDetailResponseDTO> response = notaryService.getNotaryDetail(notaryId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/notaries")
    public ResponseEntity<ApiSuccessResponse<NotaryCreateResponseDTO>> createNotary(
            @Valid @RequestBody NotaryCreateRequestDTO request) {
        ApiSuccessResponse<NotaryCreateResponseDTO> response = notaryService.createNotary(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(path = "/notaries/{notary_id}")
    public ResponseEntity<ApiSuccessResponse<NotaryUpdateResponseDTO>> updateNotary(
            @PathVariable("notary_id") UUID notaryId,
            @Valid NotaryUpdateRequestDTO request) {
        ApiSuccessResponse<NotaryUpdateResponseDTO> response = notaryService.updateNotary(notaryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/notaries/{notary_id}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteNotary(@PathVariable("notary_id") UUID notaryId) {
        ApiSuccessResponse<Void> response = notaryService.deleteNotary(notaryId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@notarySecurityService.isOwner(#notary_id, authentication)")
    @GetMapping("/notaries/{notary_id}/personal-info")
    public ResponseEntity<ApiSuccessResponse<?>> getPersonalInfo(@PathVariable @NotNull UUID notary_id, @AuthenticationPrincipal Jwt jwt  ) {
        return ResponseEntity.ok(
                ApiSuccessResponse.ok(notaryServiceImpl.getPersonalInfo(notary_id))
        );
    }

    @PreAuthorize("@notarySecurityService.isOwner(#notary_id, authentication)")
    @PutMapping(value = "/notaries/{notary_id}/personal-info",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiSuccessResponse<?>> updatePersonalInfo(
            @PathVariable @NotNull UUID notary_id,
            @ModelAttribute @Valid UpdateNotaryInfoRequest request
    ) {
        return ResponseEntity.ok(
                ApiSuccessResponse.ok(notaryServiceImpl.updatePersonalInfo(notary_id, request))
        );
    }

}
