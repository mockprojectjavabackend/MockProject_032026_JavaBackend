package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_admin_server.dto.response.IncidentResponse;
import java.util.List;
import java.util.UUID;

public interface NotaryIncidentService {
    List<IncidentResponse> getRecentIncidents(UUID notaryId, int limit);
}