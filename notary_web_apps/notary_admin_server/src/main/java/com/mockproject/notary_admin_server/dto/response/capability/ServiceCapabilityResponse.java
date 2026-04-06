package com.mockproject.notary_admin_server.dto.response.capability;

import com.mockproject.notary_admin_server.dto.request.Capability.CapabilityRequest;
import lombok.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.HashSet;
import java.util.List;
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
    private List<String> serviceArea;
    private int maxTravelDistance;
    private Set<String> languages;
    private AvailabilityDTO availability;
    private Set<HolidayDTO> appliedHolidays = new HashSet<>();
}

