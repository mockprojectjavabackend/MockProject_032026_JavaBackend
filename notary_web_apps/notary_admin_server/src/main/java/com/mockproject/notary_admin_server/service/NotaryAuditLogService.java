package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_admin_server.dto.response.AuditTrailDetailResponse;
import com.mockproject.notary_admin_server.dto.response.AuditTrailPageResponse;

import java.util.UUID;

public interface NotaryAuditLogService {
    AuditTrailPageResponse getAuditTrail(
            UUID notaryId, String timeRange, UUID userId, String action, int page, int limit);

    AuditTrailDetailResponse getAuditTrailDetail(UUID notaryId, UUID auditId);
}