package com.mockproject.notary_admin_server.controller;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.request.NotaryCreateRequestDTO;
import com.mockproject.notary_admin_server.dto.request.NotaryUpdateRequestDTO;
import com.mockproject.notary_admin_server.dto.response.*;
import com.mockproject.notary_admin_server.service.NotaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * NotaryController
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      TranMinh    create
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotaryController {
    private final NotaryService notaryService;

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

    @PutMapping("/notaries/{notary_id}")
    public ResponseEntity<ApiSuccessResponse<NotaryUpdateResponseDTO>> updateNotary(
            @PathVariable("notary_id") UUID notaryId,
            @Valid @RequestBody NotaryUpdateRequestDTO request) {
        ApiSuccessResponse<NotaryUpdateResponseDTO> response = notaryService.updateNotary(notaryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/notaries/{notary_id}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteNotary(@PathVariable("notary_id") UUID notaryId) {
        ApiSuccessResponse<Void> response = notaryService.deleteNotary(notaryId);
        return ResponseEntity.ok(response);
    }
}
