package com.mockproject.notary_admin_server.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditTrailResponse {
    private String timestamp;
    private String action;
    private String administrator;
    private String fieldChanged;
    private String beforeValue;
    private String afterValue;
}