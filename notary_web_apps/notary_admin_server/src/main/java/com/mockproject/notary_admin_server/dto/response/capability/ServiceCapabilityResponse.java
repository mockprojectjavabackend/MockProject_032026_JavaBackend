package com.mockproject.notary_admin_server.dto.response.capability;

import lombok.*;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Set;

/**
 * ServiceCapabilityResponse
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      ThoHa       create
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServiceCapabilityResponse {
    private ServiceCapabilitiesDTO serviceCapabilities;
    private String serviceArea;
    private int maxTravelDistance;
    private Set<String> languages;
    private AvailabilityDTO availability;
}

