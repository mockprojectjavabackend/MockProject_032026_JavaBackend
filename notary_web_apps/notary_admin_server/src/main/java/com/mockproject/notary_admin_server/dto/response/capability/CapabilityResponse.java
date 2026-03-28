package com.mockproject.notary_admin_server.dto.response.capability;


import com.mockproject.notary_common.constant.FixedDayOffEnum;
import lombok.*;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * CapabilityResponse
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      ThoHa       create
 */

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
