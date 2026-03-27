package com.mockproject.notary_admin_server.dto.request.Capability;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateCapabilityRequest {
    @NotNull(message = "Service capabilities can't be null")
    @Valid
    private ServiceCapabilities serviceCapabilities;

    @NotBlank(message = "Service area is required")
    private String serviceArea;

    @Min(value = 1, message = "The travel distance must be greater than 0")
    private int maxTravelDistance;

    @NotEmpty(message = "Languages is not empty")
    private Set<String> languages;

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

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private Set<LocalDate> fixedDayOff;
    }
}
