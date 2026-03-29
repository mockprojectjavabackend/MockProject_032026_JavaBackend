package com.mockproject.notary_admin_server.controller;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.response.AuditTrailResponse;
import com.mockproject.notary_admin_server.service.NotaryAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notaries")
@RequiredArgsConstructor
public class NotaryAuditLogController {

    private final NotaryAuditLogService notaryAuditLogService;

    @GetMapping("/{notaryId}/audit-trails")
    public ApiSuccessResponse<List<AuditTrailResponse>> getAuditTrail(
            @PathVariable UUID notaryId,
            @RequestParam(defaultValue = "last_day") String timeRange,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        List<AuditTrailResponse> data = notaryAuditLogService.getAuditTrail(notaryId, timeRange, page, limit);
        return ApiSuccessResponse.ok(data);
    }
}