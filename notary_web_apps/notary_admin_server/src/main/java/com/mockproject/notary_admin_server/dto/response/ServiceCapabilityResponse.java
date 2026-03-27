package com.mockproject.notary_admin_server.dto.response;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ServiceCapabilityResponse {
    private ServiceCapabilitiesDTO service_capabilities;
    private String service_area;
    private int max_travel_distance;
    private Set<String> languages;
    private AvailabilityDTO availability;
}
