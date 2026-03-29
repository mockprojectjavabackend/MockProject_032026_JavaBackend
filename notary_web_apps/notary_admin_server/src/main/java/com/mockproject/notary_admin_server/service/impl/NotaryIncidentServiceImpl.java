package com.mockproject.notary_admin_server.service.impl;

import com.mockproject.notary_admin_server.dto.response.IncidentResponse;
import com.mockproject.notary_admin_server.repository.NotaryIncidentRepository;
import com.mockproject.notary_admin_server.service.NotaryIncidentService;
import com.mockproject.notary_common.entity.notary.NotaryIncident;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotaryIncidentServiceImpl implements NotaryIncidentService {

    private final NotaryIncidentRepository notaryIncidentRepository;

    @Override
    public List<IncidentResponse> getRecentIncidents(UUID notaryId, int limit) {
        return notaryIncidentRepository
                .findByNotaryIdOrderByCreatedAtDesc(notaryId)
                .stream()
                .limit(limit)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private IncidentResponse mapToResponse(NotaryIncident incident) {
        return IncidentResponse.builder()
                .incidentId(incident.getId().toString())
                .title(incident.getDescription())
                .status(incident.getStatus().name())
                .resolutionNote(incident.getResolvedAt())
                .date(incident.getCreatedAt())
                .build();
    }
}