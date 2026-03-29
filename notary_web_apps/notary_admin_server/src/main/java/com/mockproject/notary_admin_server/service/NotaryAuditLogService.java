package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_admin_server.dto.response.AuditTrailResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface NotaryAuditLogService {
    List<AuditTrailResponse> getAuditTrail(
            UUID notaryId,
            String timeRange,
            int page,
            int limit);
}