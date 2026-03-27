package com.mockproject.notary_admin_server.dto.response;


import com.mockproject.notary_common.constant.FixedDayOffEnum;
import lombok.*;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CapabilityResponse {
    private UUID id;
    private UUID notaryId;
    private String timezone;
    private int workingDaysPerWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private FixedDayOffEnum fixedDayOff;
    private Set<HolidayDTO> appliedHolidays = new HashSet<>();
}
