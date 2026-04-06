package com.mockproject.notary_admin_server.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AuditTrailDetailResponse {
    private String id;
    private String timestamp;
    private String action;
    private String tableName; // Related entity
    private String administrator;
    private Map<String, Object> beforeValue; // Full change details
    private Map<String, Object> afterValue;
}