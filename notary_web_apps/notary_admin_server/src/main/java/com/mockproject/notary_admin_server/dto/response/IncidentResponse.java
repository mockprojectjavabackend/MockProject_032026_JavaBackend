package com.mockproject.notary_admin_server.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class IncidentResponse {
    private String incidentId;
    private String title;
    private String status;
    private LocalDateTime resolutionNote;
    private LocalDateTime date;
}