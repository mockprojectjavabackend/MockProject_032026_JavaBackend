package com.mockproject.notary_admin_server.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mockproject.notary_common.constant.FixedDayOffEnum;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AvailabilityDTO {
    private List<DayOfWeek> working_days;
    @JsonProperty("start_time")
    private LocalTime startTime;
    @JsonProperty("end_time")
    private LocalTime endTime;
    @JsonProperty("fixed_day_off")
    private FixedDayOffEnum fixedDayOff;
}
