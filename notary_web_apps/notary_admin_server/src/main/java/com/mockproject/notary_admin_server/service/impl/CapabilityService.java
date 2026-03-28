package com.mockproject.notary_admin_server.service.impl;

import com.mockproject.notary_admin_server.dto.request.Capability.CapabilityRequest;
import com.mockproject.notary_admin_server.dto.response.AvailabilityDTO;
import com.mockproject.notary_admin_server.dto.response.CapabilityResponse;
import com.mockproject.notary_admin_server.dto.response.HolidayDTO;
import com.mockproject.notary_admin_server.dto.response.ServiceCapabilityResponse;
import com.mockproject.notary_admin_server.mapper.NotaryAvailabilityMapper;
import com.mockproject.notary_admin_server.mapper.NotaryCapabilityMapper;
import com.mockproject.notary_admin_server.repository.*;
import com.mockproject.notary_admin_server.service.ICapabilityService;
import com.mockproject.notary_admin_server.service.IFederalHolidayService;
import com.mockproject.notary_common.constant.FixedDayOffEnum;
import com.mockproject.notary_common.entity.FederalHoliday;
import com.mockproject.notary_common.entity.Language;
import com.mockproject.notary_common.entity.notary.Notary;
import com.mockproject.notary_common.entity.notary.NotaryAvailability;
import com.mockproject.notary_common.entity.notary.NotaryCapability;
import com.mockproject.notary_common.entity.notary.NotaryServiceArea;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CapabilityService implements ICapabilityService {
    private final LanguageRepository languageRepository;
    private final NotaryCapabilityRepository notaryCapabilityRepository;
    private final NotaryServiceAreaRepository notaryServiceAreaRepository;
    private final NotaryAvailabilityRepository notaryAvailabilityRepository;
    private final IFederalHolidayService federalHolidayService;
    private final NotariesRepository notariesRepository;
    private final NotaryCapabilityMapper notaryCapabilityMapper;
    private final NotaryAvailabilityMapper notaryAvailabilityMapper;

    @Override
    public CapabilityResponse getCapability(UUID notaryId) {
        NotaryServiceArea notaryServiceArea = notaryServiceAreaRepository.findByNotary_Id(notaryId);
        NotaryAvailability notaryAvailability = notaryAvailabilityRepository.findByNotary_Id(notaryId);
        Set<FederalHoliday> federalHolidays = federalHolidayService.getFederalHolidays();

        Set<HolidayDTO> appliedHolidays = federalHolidays.stream().map(day -> HolidayDTO.builder()
                .date(day.getHolidayDate())
                .name(day.getHolidayName())
                .type("FEDERAL")
                .build()).collect(Collectors.toSet());

        appliedHolidays.addAll(notaryServiceArea.getState().getStateHolidays()
                .stream()
                .map(day -> HolidayDTO.builder()
                        .date(day.getHolidayDate())
                        .name(day.getHolidayName())
                        .type("STATE")
                        .build()).collect(Collectors.toSet()));


        CapabilityResponse response = CapabilityResponse.builder()
                .id(notaryAvailability.getId())
                .notaryId(notaryAvailability.getNotary().getId())
                .timezone(notaryAvailability.getTimezone())
                .workingDaysPerWeek(notaryAvailability.getWorkingDaysPerWeek())
                .startTime(notaryAvailability.getStartTime())
                .endTime(notaryAvailability.getEndTime())
                .fixedDayOff(notaryAvailability.getFixedDayOff())
                .appliedHolidays(appliedHolidays)
                .build();

        return response;
    }

    @Transactional
    @Override
    public ServiceCapabilityResponse createCapability(UUID notaryId, CapabilityRequest createCapabilityRequest) {
        Notary notary = saveLanguages(createCapabilityRequest,notaryId);
        NotaryCapability notaryCapability = notaryCapabilityRepository.findByNotary_Id(notaryId);
        if (notaryCapability != null) {
            throw new RuntimeException("Notary Capability already exists");
        }

        notaryCapability = saveCapability(createCapabilityRequest, notary);

        NotaryAvailability notaryAvailability = notaryAvailabilityRepository.findByNotary_Id(notaryId);
        if (notaryAvailability != null) {
            throw new RuntimeException("Notary Avaibility already exists");
        }
        notaryAvailability = saveAvailability(createCapabilityRequest, notary);

        AvailabilityDTO availabilityDTO = notaryAvailabilityMapper.toAvailabilityDTO(notaryAvailability);
        availabilityDTO.setWorking_days(getWorkingDays(notaryAvailability.getWorkingDaysPerWeek(), notaryAvailability.getFixedDayOff()));

        NotaryServiceArea notaryServiceArea = notaryServiceAreaRepository.findByCountyName(createCapabilityRequest.getServiceArea());
        notaryServiceArea.setNotary(notary);

        ServiceCapabilityResponse response =  ServiceCapabilityResponse.builder()
                .service_capabilities(notaryCapabilityMapper.toServiceCapabilitiesDTO(notaryCapability))
                .availability(availabilityDTO)
                .max_travel_distance(Math.round(notaryCapability.getMaxDistance()))
                .languages(notary.getLanguages().stream().map(Language::getLangName).collect(Collectors.toSet()))
                .service_area(notaryServiceArea.getCountyName())
                .build();

        return response;
    }

    @Transactional
    @Override
    public ServiceCapabilityResponse updateCapability(UUID notaryId, CapabilityRequest capabilityRequest) {
        Notary notary = saveLanguages(capabilityRequest,notaryId);
        NotaryCapability notaryCapability = notaryCapabilityRepository.findByNotary_Id(notaryId);
        if (notaryCapability == null) {
            throw new RuntimeException("Notary Capability doesn't exist");
        }

        notaryCapability.setRon(capabilityRequest.getServiceCapabilities().isRon());
        notaryCapability.setMobile(capabilityRequest.getServiceCapabilities().isMobileNotary());
        notaryCapability.setLoanSigning(capabilityRequest.getServiceCapabilities().isLoanSigning());
        notaryCapability.setApostilleRelatedSupport(capabilityRequest.getServiceCapabilities().isApostilleSupport());
        notaryCapability.setMaxDistance((float)capabilityRequest.getMaxTravelDistance());

        NotaryAvailability notaryAvailability = notaryAvailabilityRepository.findByNotary_Id(notaryId);
        if (notaryAvailability == null) {
            throw new RuntimeException("Notary Avaibility doesn't exist");
        }
        notaryAvailability.setWorkingDaysPerWeek(capabilityRequest.getAvailability().getWorkingDays().size());
        notaryAvailability.setStartTime(capabilityRequest.getAvailability().getStartTime());
        notaryAvailability.setEndTime(capabilityRequest.getAvailability().getEndTime());
        notaryAvailability.setFixedDayOff(capabilityRequest.getAvailability().getFixedDayOff()
                        .stream()
                        .map(day -> FixedDayOffEnum.valueOf(day.getDayOfWeek().name()))
                        .findFirst()
                        .orElse(null)
                );

        NotaryServiceArea notaryServiceArea = notaryServiceAreaRepository.findByCountyName(capabilityRequest.getServiceArea());
        if (notaryServiceArea == null) {
            throw new RuntimeException("Notary Service Area doesn't exist");
        }
        notaryServiceArea.setNotary(notary);

        AvailabilityDTO availabilityDTO = notaryAvailabilityMapper.toAvailabilityDTO(notaryAvailability);
        availabilityDTO.setWorking_days(
                getWorkingDays(notaryAvailability.getWorkingDaysPerWeek(),
                            notaryAvailability.getFixedDayOff()
                )
        );

        ServiceCapabilityResponse response =  ServiceCapabilityResponse.builder()
                .service_capabilities(notaryCapabilityMapper.toServiceCapabilitiesDTO(notaryCapability))
                .availability(availabilityDTO)
                .max_travel_distance(Math.round(notaryCapability.getMaxDistance()))
                .languages(notary.getLanguages().stream().map(Language::getLangName).collect(Collectors.toSet()))
                .service_area(notaryServiceArea.getCountyName())
                .build();

        return response;
    }

    public NotaryCapability saveCapability(CapabilityRequest createCapabilityRequest, Notary notary) {
        NotaryCapability notaryCapability = NotaryCapability.builder()
                .ron(createCapabilityRequest.getServiceCapabilities().isRon())
                .mobile(createCapabilityRequest.getServiceCapabilities().isMobileNotary())
                .loanSigning(createCapabilityRequest.getServiceCapabilities().isLoanSigning())
                .apostilleRelatedSupport(createCapabilityRequest.getServiceCapabilities().isApostilleSupport())
                .maxDistance((float)createCapabilityRequest.getMaxTravelDistance())
                .notary(notary)
                .build();

        return notaryCapabilityRepository.save(notaryCapability);
    }

    public NotaryAvailability saveAvailability(CapabilityRequest createCapabilityRequest, Notary notary) {
        NotaryAvailability notaryAvailability;
        if(createCapabilityRequest.getAvailability() != null){
            notaryAvailability = NotaryAvailability.builder()
                    .timezone(ZoneId.systemDefault().toString())
                    .startTime(createCapabilityRequest.getAvailability().getStartTime())
                    .endTime(createCapabilityRequest.getAvailability().getEndTime())
                    .workingDaysPerWeek(createCapabilityRequest.getAvailability().getWorkingDays().size())
                    .fixedDayOff(createCapabilityRequest.getAvailability().getFixedDayOff()
                            .stream()
                            .map(day -> FixedDayOffEnum.valueOf(day.getDayOfWeek().name()))
                            .findFirst()
                            .orElse(null))
                    .notary(notary)
                    .build();
        }else {
            notaryAvailability = NotaryAvailability.builder()
                    .timezone(ZoneId.systemDefault().toString())
                    .startTime(LocalTime.parse("07:00:00"))
                    .endTime(LocalTime.parse("17:00:00"))
                    .workingDaysPerWeek(DayOfWeek.values().length)
                    .notary(notary)
                    .build();
        }

        return notaryAvailabilityRepository.save(notaryAvailability);
    }

    public Notary saveLanguages(CapabilityRequest createCapabilityRequest, UUID notaryId) {
        if(createCapabilityRequest.getLanguages().isEmpty()){
            throw new RuntimeException("Language list is empty");
        }
        List<String> requestedLanguages = createCapabilityRequest.getLanguages();
        Set<Language> newLanguages = languageRepository.findByLangNameIn(new HashSet<>(requestedLanguages));
        Notary notary = notariesRepository.findById(notaryId).orElseThrow(()->new RuntimeException("Lỗi"));
        notary.getLanguages().clear();
        notary.getLanguages().addAll(newLanguages);

        return notariesRepository.save(notary);

    }

    public List<DayOfWeek>  getWorkingDays(int workingDaysPerWeek, FixedDayOffEnum offDay){
        return Arrays.stream(DayOfWeek.values())
                .filter(day -> offDay == null || !day.name().equals(offDay.name()))
                .limit(workingDaysPerWeek)
                .toList();
    }
}
