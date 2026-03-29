package com.mockproject.notary_admin_server.controller;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.request.UpdateNotaryInfoRequest;
import com.mockproject.notary_admin_server.service.NotaryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
@Validated
@RequestMapping("/api/notaries")
public class NotaryController {
    private final NotaryService notaryService;

    public NotaryController(NotaryService notaryService) {
        this.notaryService = notaryService;
    }

    @GetMapping("/{notary_id}/personal-info")
    public ResponseEntity<ApiSuccessResponse<?>> getPersonalInfo(@PathVariable @NotNull UUID notary_id) {
        boolean isAdmin = false;
        boolean isOwner = true;
        if (!isAdmin && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access notary with id: " + notary_id);
        }
        if (isAdmin) {
            return ResponseEntity.ok(ApiSuccessResponse.ok(notaryService.getNotaryInfoForAdmin(notary_id)));
        }
        return ResponseEntity.ok(ApiSuccessResponse.ok(notaryService.getPersonalInfo(notary_id)));
    }

    @PutMapping("/{notary_id}/personal-info")
    public ResponseEntity<ApiSuccessResponse<?>> updatePersonalInfo(
            @PathVariable @NotNull UUID notary_id,
            @Valid @RequestBody UpdateNotaryInfoRequest request) {
        boolean isAdmin = true;
        boolean isOwner = true;
        if (!isAdmin && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access notary with id: " + notary_id);
        }
        if (isAdmin) {
            return ResponseEntity.ok(ApiSuccessResponse.ok(notaryService.updatePersonalInfoByAdmin(notary_id, request)));
        }
        return ResponseEntity.ok(ApiSuccessResponse.ok(notaryService.updatePersonalInfo(notary_id, request)));
    }
}
