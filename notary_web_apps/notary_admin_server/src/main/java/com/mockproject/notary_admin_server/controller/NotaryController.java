package com.mockproject.notary_admin_server.controller;

import com.mockproject.notary_admin_server.dto.ApiResponse;
import com.mockproject.notary_admin_server.dto.request.UpdateNotaryInfoRequest;
import com.mockproject.notary_admin_server.service.NotaryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * NotaryController
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      PhamTam      create
 */

@RestController
@RequestMapping("/api/notaries")
public class NotaryController {
    private final NotaryService notaryService;

    public NotaryController(NotaryService notaryService) {
        this.notaryService = notaryService;
    }

    @GetMapping("/{notary_id}/personal-info")
    public ResponseEntity<ApiResponse<?>> getPersonalInfo(@PathVariable UUID notary_id) {
        boolean isAdmin = false; // TODO: lấy từ JWT sau
        if (isAdmin) {
            return ResponseEntity.ok(ApiResponse.success(notaryService.getNotaryInfoForAdmin(notary_id)));
        }
        return ResponseEntity.ok(ApiResponse.success(notaryService.getPersonalInfo(notary_id)));
    }

    @PutMapping("/{notary_id}/personal-info")
    public ResponseEntity<ApiResponse<?>> updatePersonalInfo(
            @PathVariable UUID notary_id,
            @Valid @RequestBody UpdateNotaryInfoRequest request) {
        boolean isAdmin = false; // TODO: lấy từ JWT sau

        if (isAdmin) {
            return ResponseEntity.ok(ApiResponse.success(notaryService.updatePersonalInfoByAdmin(notary_id, request)));
        }
        return ResponseEntity.ok(ApiResponse.success(notaryService.updatePersonalInfo(notary_id, request)));
    }
}
