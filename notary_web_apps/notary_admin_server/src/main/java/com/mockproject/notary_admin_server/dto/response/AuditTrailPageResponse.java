package com.mockproject.notary_admin_server.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AuditTrailPageResponse {
    private List<AuditTrailResponse> data;
    private MetaResponse meta;
}