package com.mockproject.notary_admin_server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mockproject.notary_admin_server.dto.request.Capability.CapabilityRequest;
import com.mockproject.notary_admin_server.dto.response.capability.AvailabilityDTO;
import com.mockproject.notary_admin_server.dto.response.capability.ServiceCapabilitiesDTO;
import com.mockproject.notary_admin_server.dto.response.capability.ServiceCapabilityResponse;
import com.mockproject.notary_admin_server.service.ICapabilityService;
import com.mockproject.notary_common.constant.FixedDayOffEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.jackson.property-naming-strategy=SNAKE_CASE"
})
class CapabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .build();

    @MockitoBean
    private ICapabilityService capabilityService;


    private CapabilityRequest buildValidRequest() {
        CapabilityRequest.ServiceCapabilities sc = new CapabilityRequest.ServiceCapabilities();
        sc.setMobileNotary(true);
        sc.setRon(true);
        sc.setLoanSigning(true);
        sc.setApostilleSupport(true);

        CapabilityRequest.Availability availability = new CapabilityRequest.Availability();
        availability.setWorkingDays(List.of(
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
        ));
        availability.setStartTime(LocalTime.of(8, 0));
        availability.setEndTime(LocalTime.of(17, 0));
        availability.setFixedDayOff(FixedDayOffEnum.SUNDAY);

        CapabilityRequest.ServiceArea area = new CapabilityRequest.ServiceArea();
        area.setStateId(UUID.fromString("33330000-0000-0003-0000-000000000003"));
        area.setCountryName("District 7");

        CapabilityRequest request = new CapabilityRequest();
        request.setServiceCapabilities(sc);
        request.setServiceArea(List.of(area));
        request.setMaxTravelDistance(60);
        request.setLanguages(List.of("English", "Vietnamese"));
        request.setAvailability(availability);

        return request;
    }


    private ServiceCapabilityResponse buildResponse(CapabilityRequest request) {
        return ServiceCapabilityResponse.builder()
                .serviceArea(
                        request.getServiceArea().stream()
                                .map(CapabilityRequest.ServiceArea::getCountryName)
                                .toList()
                )
                .maxTravelDistance(request.getMaxTravelDistance())
                .languages(new HashSet<>(request.getLanguages()))
                .serviceCapabilities(
                        ServiceCapabilitiesDTO.builder()
                                .mobile(request.getServiceCapabilities().isMobileNotary())
                                .ron(request.getServiceCapabilities().isRon())
                                .loanSigning(request.getServiceCapabilities().isLoanSigning())
                                .apostilleRelatedSupport(request.getServiceCapabilities().isApostilleSupport())
                                .build()
                )
                .availability(
                        AvailabilityDTO.builder()
                                .working_days(request.getAvailability().getWorkingDays())
                                .startTime(request.getAvailability().getStartTime())
                                .endTime(request.getAvailability().getEndTime())
                                .fixedDayOff(request.getAvailability().getFixedDayOff())
                                .build()
                )
                .appliedHolidays(null)
                .build();
    }

    // ================= GET =================

    @Test
    void should_get_capability_success() throws Exception {
        UUID notaryId = UUID.randomUUID();

        ServiceCapabilityResponse response = new ServiceCapabilityResponse();
        response.setServiceArea(List.of("District 3")); // ✅ FIX

        when(capabilityService.getCapability(notaryId)).thenReturn(response);

        mockMvc.perform(get("/api/notaries/{notaryId}/service_capability", notaryId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.service_area[0]").value("District 3"));

        verify(capabilityService).getCapability(notaryId);
    }

    // ================= CREATE =================

    @Test
    void should_create_capability_success() throws Exception {
        UUID notaryId = UUID.randomUUID();

        CapabilityRequest request = buildValidRequest();
        ServiceCapabilityResponse response = buildResponse(request);

        when(capabilityService.createCapability(eq(notaryId), any(CapabilityRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/notaries/{notaryId}/service_capability", notaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.service_area[0]").value("District 7"))
                .andExpect(jsonPath("$.data.max_travel_distance").value(60))
                .andExpect(jsonPath("$.data.languages[0]").value("English"));

        verify(capabilityService).createCapability(eq(notaryId), any(CapabilityRequest.class));
    }

    // ================= UPDATE =================

    @Test
    void should_update_capability_success() throws Exception {
        UUID notaryId = UUID.randomUUID();

        CapabilityRequest request = buildValidRequest();
        ServiceCapabilityResponse response = buildResponse(request);

        when(capabilityService.updateCapability(eq(notaryId), any(CapabilityRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/notaries/{notaryId}/service_capability", notaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.service_area[0]").value("District 7"));

        verify(capabilityService).updateCapability(eq(notaryId), any(CapabilityRequest.class));
    }

}