package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_admin_server.dto.request.Capability.CapabilityRequest;
import com.mockproject.notary_admin_server.dto.response.capability.AvailabilityDTO;
import com.mockproject.notary_admin_server.dto.response.capability.ServiceCapabilitiesDTO;
import com.mockproject.notary_admin_server.dto.response.capability.ServiceCapabilityResponse;
import com.mockproject.notary_admin_server.exception.AppException;
import com.mockproject.notary_admin_server.mapper.NotaryAvailabilityMapper;
import com.mockproject.notary_admin_server.mapper.NotaryCapabilityMapper;
import com.mockproject.notary_admin_server.repository.*;
import com.mockproject.notary_admin_server.service.impl.CapabilityService;
import com.mockproject.notary_common.constant.FixedDayOffEnum;
import com.mockproject.notary_common.entity.Language;
import com.mockproject.notary_common.entity.State;
import com.mockproject.notary_common.entity.notary.Notary;
import com.mockproject.notary_common.entity.notary.NotaryAvailability;
import com.mockproject.notary_common.entity.notary.NotaryCapability;
import com.mockproject.notary_common.entity.notary.NotaryServiceArea;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class CapabilityServiceTest {

    @InjectMocks
    private CapabilityService capabilityService;

    @Mock
    private LanguageRepository languageRepository;
    @Mock
    private NotaryCapabilityRepository notaryCapabilityRepository;
    @Mock
    private NotaryServiceAreaRepository notaryServiceAreaRepository;
    @Mock
    private NotaryAvailabilityRepository notaryAvailabilityRepository;
    @Mock
    private IFederalHolidayService federalHolidayService;
    @Mock
    private NotariesRepository notariesRepository;
    @Mock
    private StateRepository stateRepository;
    @Mock
    private NotaryCapabilityMapper notaryCapabilityMapper;
    @Mock
    private NotaryAvailabilityMapper notaryAvailabilityMapper;



    private CapabilityRequest buildRequest() {
        CapabilityRequest request = new CapabilityRequest();

        CapabilityRequest.ServiceCapabilities sc = new CapabilityRequest.ServiceCapabilities();
        sc.setMobileNotary(true);
        sc.setRon(true);
        sc.setLoanSigning(true);
        sc.setApostilleSupport(true);

        CapabilityRequest.Availability av = new CapabilityRequest.Availability();
        av.setWorkingDays(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));
        av.setStartTime(LocalTime.of(8, 0));
        av.setEndTime(LocalTime.of(17, 0));
        av.setFixedDayOff(FixedDayOffEnum.SUNDAY);

        CapabilityRequest.ServiceArea serviceArea = new CapabilityRequest.ServiceArea();
        serviceArea.setCountryName("District 7");
        serviceArea.setStateId(UUID.fromString("33330000-0000-0003-0000-000000000003"));
        request.setServiceCapabilities(sc);
        request.setServiceArea(List.of(serviceArea));
        request.setMaxTravelDistance(50);
        request.setLanguages(List.of("English"));
        request.setAvailability(av);

        return request;
    }

    private Notary buildNotary(UUID id) {
        Notary notary = new Notary();
        notary.setId(id);
        notary.setLanguages(new HashSet<>());
        return notary;
    }



    // ================= TEST CREATE =================

    @Test
    void should_create_capability_success() {
        UUID notaryId = UUID.randomUUID();
        CapabilityRequest request = buildRequest();

        Notary notary = buildNotary(notaryId);

        Language lang = new Language();
        lang.setLangName("English");

        State state = new State();
        state.setId(request.getServiceArea().get(0).getStateId());

        when(languageRepository.findByLangNameIn(any())).thenReturn(Set.of(lang));
        when(notariesRepository.findById(notaryId)).thenReturn(Optional.of(notary));
        when(notariesRepository.save(any())).thenReturn(notary);

        when(notaryCapabilityRepository.findByNotary_Id(notaryId)).thenReturn(null);
        when(notaryAvailabilityRepository.findByNotary_Id(notaryId)).thenReturn(null);

        when(notaryCapabilityRepository.save(any())).thenReturn(new NotaryCapability());
        when(notaryAvailabilityRepository.save(any())).thenReturn(new NotaryAvailability());

        when(stateRepository.findById(any())).thenReturn(Optional.of(state));

        when(notaryCapabilityMapper.toServiceCapabilitiesDTO(any()))
                .thenReturn(new ServiceCapabilitiesDTO());
        when(notaryAvailabilityMapper.toAvailabilityDTO(any()))
                .thenReturn(new AvailabilityDTO());

        ServiceCapabilityResponse result =
                capabilityService.createCapability(notaryId, request);

        assertNotNull(result);

        verify(notaryCapabilityRepository).save(any());
        verify(notaryAvailabilityRepository).save(any());
        verify(notaryServiceAreaRepository).saveAll(any()); // ✅ FIX
    }

    // ================= TEST EXCEPTION =================

    @Test
    void should_throw_when_capability_existed() {
        UUID notaryId = UUID.randomUUID();
        CapabilityRequest request = buildRequest();

        Notary notary = buildNotary(notaryId);

        when(languageRepository.findByLangNameIn(any())).thenReturn(Set.of());
        when(notariesRepository.findById(notaryId)).thenReturn(Optional.of(notary));
        when(notariesRepository.save(any())).thenReturn(notary);

        when(notaryCapabilityRepository.findByNotary_Id(notaryId))
                .thenReturn(new NotaryCapability());

        assertThrows(AppException.class, () ->
                capabilityService.createCapability(notaryId, request)
        );
    }

    // ================= TEST GET =================

    @Test
    void should_get_capability_success() {
        UUID notaryId = UUID.randomUUID();

        Notary notary = buildNotary(notaryId);

        NotaryCapability capability = new NotaryCapability();
        capability.setMaxDistance(60);

        NotaryAvailability availability = new NotaryAvailability();
        availability.setWorkingDaysPerWeek(2);
        availability.setFixedDayOff(FixedDayOffEnum.SUNDAY);

        NotaryServiceArea area = new NotaryServiceArea();
        area.setCountyName("District 1");

        State state = new State();
        state.setStateHolidays(new HashSet<>());
        area.setState(state);

        when(notariesRepository.findById(notaryId)).thenReturn(Optional.of(notary));
        when(notaryCapabilityRepository.findByNotary_Id(notaryId)).thenReturn(capability);
        when(notaryAvailabilityRepository.findByNotary_Id(notaryId)).thenReturn(availability);

        when(notaryServiceAreaRepository.findByNotary_Id(notaryId))
                .thenReturn(List.of(area));

        when(notaryCapabilityMapper.toServiceCapabilitiesDTO(any()))
                .thenReturn(new ServiceCapabilitiesDTO());
        when(notaryAvailabilityMapper.toAvailabilityDTO(any()))
                .thenReturn(new AvailabilityDTO());

        when(federalHolidayService.getFederalHolidays()).thenReturn(Set.of());

        ServiceCapabilityResponse result =
                capabilityService.getCapability(notaryId);

        assertNotNull(result);
    }

    // ================= TEST UPDATE =================

    @Test
    void should_update_capability_success() {
        UUID notaryId = UUID.randomUUID();
        CapabilityRequest request = buildRequest();

        Notary notary = buildNotary(notaryId);

        NotaryCapability capability = new NotaryCapability();
        NotaryAvailability availability = new NotaryAvailability();

        State state = new State();
        state.setId(request.getServiceArea().get(0).getStateId());

        when(languageRepository.findByLangNameIn(any())).thenReturn(Set.of());
        when(notariesRepository.findById(notaryId)).thenReturn(Optional.of(notary));
        when(notariesRepository.save(any())).thenReturn(notary);

        when(notaryCapabilityRepository.findByNotary_Id(notaryId)).thenReturn(capability);
        when(notaryAvailabilityRepository.findByNotary_Id(notaryId)).thenReturn(availability);

        when(stateRepository.findById(any())).thenReturn(Optional.of(state));

        when(notaryCapabilityMapper.toServiceCapabilitiesDTO(any()))
                .thenReturn(new ServiceCapabilitiesDTO());
        when(notaryAvailabilityMapper.toAvailabilityDTO(any()))
                .thenReturn(new AvailabilityDTO());

        ServiceCapabilityResponse result =
                capabilityService.updateCapability(notaryId, request);

        assertNotNull(result);

        assertFalse(notary.getServiceAreas().isEmpty());
    }
}