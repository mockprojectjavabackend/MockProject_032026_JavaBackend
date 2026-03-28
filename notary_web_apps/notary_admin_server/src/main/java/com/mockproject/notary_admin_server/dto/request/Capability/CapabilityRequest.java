package com.mockproject.notary_admin_server.dto.request.Capability;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mockproject.notary_admin_server.validation.UniqueLanguageElements;
import com.mockproject.notary_common.constant.FixedDayOffEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

/**
 * CapabilityRequest
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      ThoHa       create
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CapabilityRequest {
    @NotNull(message = "Service capabilities can't be null")
    @Valid
    private ServiceCapabilities serviceCapabilities;

    @NotBlank(message = "Service area is required")
    private String serviceArea;

    @Min(value = 1, message = "The travel distance must be greater than 0")
    private int maxTravelDistance;

    @NotEmpty(message = "Languages is not empty")
    @UniqueLanguageElements
    private List<String> languages;

    @Valid
    private Availability availability;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ServiceCapabilities{
        private boolean mobileNotary;
        private boolean ron;
        private boolean loanSigning;
        private boolean apostilleSupport;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Availability{
        @NotEmpty(message = "Working day is not empty")
        private List<DayOfWeek> workingDays;

        @NotNull(message = "Start time is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime startTime;

        @NotNull(message = "End time is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime endTime;

        private FixedDayOffEnum fixedDayOff;
    }
}
