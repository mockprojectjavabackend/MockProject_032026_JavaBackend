package com.mockproject.notary_admin_server.dto.response.capability;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mockproject.notary_common.constant.FixedDayOffEnum;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * AvailabilityDTO
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
public class AvailabilityDTO {
    private List<DayOfWeek> working_days;
    @JsonProperty("start_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonProperty("end_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    @JsonProperty("fixed_day_off")
    private FixedDayOffEnum fixedDayOff;
}
