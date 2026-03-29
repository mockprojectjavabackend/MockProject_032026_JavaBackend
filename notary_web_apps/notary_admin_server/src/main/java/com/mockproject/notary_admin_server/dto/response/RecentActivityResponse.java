package com.mockproject.notary_admin_server.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecentActivityResponse {
    private String actionType;
    private String description;
    private String performedBy;
    private String timestamp;
}