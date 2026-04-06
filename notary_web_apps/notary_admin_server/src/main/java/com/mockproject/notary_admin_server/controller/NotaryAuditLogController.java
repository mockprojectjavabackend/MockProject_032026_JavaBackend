package com.mockproject.notary_admin_server.controller;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.response.AuditTrailDetailResponse;
import com.mockproject.notary_admin_server.dto.response.AuditTrailPageResponse;
import com.mockproject.notary_admin_server.service.NotaryAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notaries")
@RequiredArgsConstructor
public class NotaryAuditLogController {

    private final NotaryAuditLogService notaryAuditLogService;

    @GetMapping("/{notaryId}/audit-trails")
    public ApiSuccessResponse<AuditTrailPageResponse> getAuditTrail(
            @PathVariable UUID notaryId,
            @RequestParam(defaultValue = "last_day") String timeRange,
            @RequestParam(required = false) UUID userId, // ← THÊM
            @RequestParam(required = false) String action, // ← THÊM
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        return ApiSuccessResponse.ok(
                notaryAuditLogService.getAuditTrail(
                        notaryId, timeRange, userId, action, page, limit));
    }

    @GetMapping("/{notaryId}/audit-trails/{auditId}")
    public ApiSuccessResponse<AuditTrailDetailResponse> getAuditTrailDetail(
            @PathVariable UUID notaryId,
            @PathVariable UUID auditId) {

        return ApiSuccessResponse.ok(
                notaryAuditLogService.getAuditTrailDetail(notaryId, auditId));
    }
}