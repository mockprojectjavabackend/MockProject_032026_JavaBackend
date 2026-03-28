package com.mockproject.notary_admin_server.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mockproject.notary_common.constant.FixedDayOffEnum;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

