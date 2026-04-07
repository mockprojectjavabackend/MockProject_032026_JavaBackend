package com.mockproject.notary_admin_server.service.impl;

import com.mockproject.notary_admin_server.dto.request.Capability.CapabilityRequest;
import com.mockproject.notary_admin_server.dto.response.capability.AvailabilityDTO;
import com.mockproject.notary_admin_server.dto.response.capability.CapabilityResponse;
import com.mockproject.notary_admin_server.dto.response.capability.HolidayDTO;
import com.mockproject.notary_admin_server.dto.response.capability.ServiceCapabilityResponse;
import com.mockproject.notary_admin_server.exception.AppException;
import com.mockproject.notary_admin_server.exception.errorCode.BaseErrorCode;
import com.mockproject.notary_admin_server.mapper.NotaryAvailabilityMapper;
import com.mockproject.notary_admin_server.mapper.NotaryCapabilityMapper;
import com.mockproject.notary_admin_server.repository.*;
import com.mockproject.notary_admin_server.service.ICapabilityService;
import com.mockproject.notary_admin_server.service.IFederalHolidayService;
import com.mockproject.notary_common.constant.FixedDayOffEnum;
import com.mockproject.notary_common.entity.Language;
import com.mockproject.notary_common.entity.State;
import com.mockproject.notary_common.entity.notary.Notary;
import com.mockproject.notary_common.entity.notary.NotaryAvailability;
import com.mockproject.notary_common.entity.notary.NotaryCapability;
import com.mockproject.notary_common.entity.notary.NotaryServiceArea;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CapabilityService
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      ThoHa       create
 */

@Service
@RequiredArgsConstructor
public class CapabilityService implements ICapabilityService {

    private final LanguageRepository languageRepository;
    private final NotaryCapabilityRepository notaryCapabilityRepository;
    private final NotaryServiceAreaRepository notaryServiceAreaRepository;
    private final NotaryAvailabilityRepository notaryAvailabilityRepository;
    private final IFederalHolidayService federalHolidayService;
    private final NotaryRepository notariesRepository;
    private final StateRepository stateRepository;
    private final NotaryCapabilityMapper notaryCapabilityMapper;
    private final NotaryAvailabilityMapper notaryAvailabilityMapper;

    @Override
    public ServiceCapabilityResponse getCapability(UUID notaryId) {

        Notary notary = notariesRepository.findById(notaryId)
                .orElseThrow(()-> new AppException(BaseErrorCode.NOTARY_NOT_FOUND));

        NotaryCapability notaryCapability = getCapabilityByNotaryOrThrow(notaryId);

        List<NotaryServiceArea> notaryServiceArea = getNotaryServiceAreaByNotaryOrThrow(notaryId);

        NotaryAvailability notaryAvailability = getAvailabilityByNotaryOrThrow(notaryId);

        Set<HolidayDTO> appliedHolidays = buildAppliedHoliday(notaryServiceArea);

        return buildResponse(notary, notaryCapability, notaryAvailability, notaryServiceArea, appliedHolidays);

    }

    @Transactional
    @Override
    public ServiceCapabilityResponse createCapability(UUID notaryId, CapabilityRequest createCapabilityRequest) {

        Notary notary = saveLanguages(createCapabilityRequest,notaryId);

        if (notaryCapabilityRepository.findByNotary_Id(notaryId) != null) {
            throw new AppException(BaseErrorCode.NOTARY_CAPABILITY_EXISTED);
        }

        if (notaryAvailabilityRepository.findByNotary_Id(notaryId) != null) {
            throw new AppException(BaseErrorCode.NOTARY_AVAILABILITY_EXISTED);
        }

        NotaryCapability notaryCapability = saveCapability(createCapabilityRequest, notary);

        NotaryAvailability notaryAvailability = saveAvailability(createCapabilityRequest, notary);

        List<NotaryServiceArea> serviceAreas = createCapabilityRequest.getServiceArea()
                .stream()
                .map(item -> {
                    State state = stateRepository.findById(item.getStateId())
                            .orElseThrow(() -> new AppException(BaseErrorCode.STATE_NOT_FOUND));

                    return NotaryServiceArea.builder()
                            .countyName(item.getCountryName())
                            .state(state)
                            .notary(notary)
                            .createdAt(LocalDateTime.now())
                            .build();
                })
                .toList();

        notaryServiceAreaRepository.saveAll(serviceAreas);

        return buildResponse(notary, notaryCapability, notaryAvailability, serviceAreas, null);
    }

    @Transactional
    @Override
    public ServiceCapabilityResponse updateCapability(UUID notaryId, CapabilityRequest request) {

        Notary notary = saveLanguages(request,notaryId);

        NotaryCapability notaryCapability = getCapabilityByNotaryOrThrow(notaryId);
        NotaryAvailability notaryAvailability = getAvailabilityByNotaryOrThrow(notaryId);

        updateNotaryCapability(notaryCapability, request);
        updateNotaryAvailability(notaryAvailability, request);

        notary.getServiceAreas().clear();

        List<NotaryServiceArea> notaryServiceAreasNew = request.getServiceArea()
            .stream()
            .map(item -> {
                State state = stateRepository.findById(item.getStateId())
                        .orElseThrow();

                return NotaryServiceArea.builder()
                        .countyName(item.getCountryName())
                        .state(state)
                        .notary(notary)
                        .createdAt(LocalDateTime.now())
                        .build();
            })
            .toList();

        notary.getServiceAreas().addAll(notaryServiceAreasNew);

        return buildResponse(notary, notaryCapability, notaryAvailability, notaryServiceAreasNew, null);
    }


    private ServiceCapabilityResponse buildResponse(Notary notary,
                                                    NotaryCapability notaryCapability,
                                                    NotaryAvailability notaryAvailability,
                                                    List<NotaryServiceArea> notaryServiceArea,
                                                    Set<HolidayDTO> appliedHolidays
                                                    ){
        AvailabilityDTO availabilityDTO = notaryAvailabilityMapper.toAvailabilityDTO(notaryAvailability);

        availabilityDTO.setWorking_days(
                getWorkingDays(notaryAvailability.getWorkingDaysPerWeek(),
                        notaryAvailability.getFixedDayOff()
                )
        );

        ServiceCapabilityResponse response =  ServiceCapabilityResponse.builder()
                .serviceCapabilities(notaryCapabilityMapper.toServiceCapabilitiesDTO(notaryCapability))
                .availability(availabilityDTO)
                .maxTravelDistance(Math.round(notaryCapability.getMaxDistance()))
                .languages(notary.getLanguages()
                        .stream()
                        .map(Language::getLangName)
                        .collect(Collectors.toSet()))
                .serviceArea(notaryServiceArea.stream().map(NotaryServiceArea::getCountyName).toList())
                .appliedHolidays(appliedHolidays)
                .build();

        return response;
    }

    private Set<HolidayDTO> buildAppliedHoliday(List<NotaryServiceArea> notaryServiceAreas){

        Set<HolidayDTO> holidays = federalHolidayService.getFederalHolidays()
                .stream()
                .map(day -> HolidayDTO.builder()
                            .date(day.getHolidayDate())
                            .name(day.getHolidayName())
                            .type("FEDERAL")
                            .build()
                ).collect(Collectors.toSet());
        for(NotaryServiceArea notaryServiceArea : notaryServiceAreas){
            holidays.addAll(notaryServiceArea.getState().getStateHolidays()
                    .stream()
                    .map(day -> HolidayDTO.builder()
                            .date(day.getHolidayDate())
                            .name(day.getHolidayName())
                            .type("STATE")
                            .build()).collect(Collectors.toSet()));
        }


        return holidays;
    }

    private NotaryCapability getCapabilityByNotaryOrThrow(UUID notaryId) {
        NotaryCapability notaryCapability = notaryCapabilityRepository.findByNotary_Id(notaryId);
        if (notaryCapability == null) {
            throw new AppException(
                    BaseErrorCode.NOTARY_CAPABILITY_NOT_FOUND,
                    Map.of("notaryId", notaryId)
            );
        }

        return notaryCapability;
    }

    private NotaryAvailability getAvailabilityByNotaryOrThrow(UUID notaryId) {
        NotaryAvailability notaryAvailability = notaryAvailabilityRepository.findByNotary_Id(notaryId);
        if (notaryAvailability == null) {
            throw new AppException(
                    BaseErrorCode.NOTARY_AVAILABILITY_NOT_FOUND,
                    Map.of("notaryId", notaryId)
            );
        }

        return notaryAvailability;
    }

    private List<NotaryServiceArea> getNotaryServiceAreaByNotaryOrThrow(UUID notaryId) {
        List<NotaryServiceArea> notaryServiceArea = notaryServiceAreaRepository.findByNotary_Id(notaryId);
        if (notaryServiceArea.isEmpty()) {
            throw new AppException(
                    BaseErrorCode.NOTARY_SERVICE_AREA_NOT_FOUND,
                    Map.of("notaryId", notaryId)
            );
        }

        return notaryServiceArea;
    }

    private NotaryServiceArea getServiceAreaByCountyNameOrThrow(String county) {
        NotaryServiceArea notaryServiceArea = notaryServiceAreaRepository.findByCountyName(county);
        if (notaryServiceArea == null) {
            throw new AppException(
                    BaseErrorCode.NOTARY_SERVICE_AREA_COUNTY_NOT_FOUND
            );
        }

        return notaryServiceArea;
    }

    private NotaryCapability saveCapability(CapabilityRequest createCapabilityRequest, Notary notary) {
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

    private NotaryAvailability saveAvailability(CapabilityRequest createCapabilityRequest, Notary notary) {
        NotaryAvailability notaryAvailability;
        if(createCapabilityRequest.getAvailability() != null){
            notaryAvailability = NotaryAvailability.builder()
                    .timezone(ZoneId.systemDefault().toString())
                    .startTime(createCapabilityRequest.getAvailability().getStartTime())
                    .endTime(createCapabilityRequest.getAvailability().getEndTime())
                    .workingDaysPerWeek(createCapabilityRequest.getAvailability().getWorkingDays().size())
                    .fixedDayOff(createCapabilityRequest.getAvailability().getFixedDayOff())
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

    private Notary saveLanguages(CapabilityRequest createCapabilityRequest, UUID notaryId) {
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

    private void updateNotaryAvailability(NotaryAvailability notaryAvailability, CapabilityRequest capabilityRequest) {
        if(capabilityRequest.getAvailability() != null){
            notaryAvailability.setWorkingDaysPerWeek(capabilityRequest.getAvailability().getWorkingDays().size());
            notaryAvailability.setStartTime(capabilityRequest.getAvailability().getStartTime());
            notaryAvailability.setEndTime(capabilityRequest.getAvailability().getEndTime());
            notaryAvailability.setFixedDayOff(capabilityRequest.getAvailability().getFixedDayOff());
        }
    }

    private void updateNotaryCapability(NotaryCapability notaryCapability, CapabilityRequest capabilityRequest) {
        notaryCapability.setRon(capabilityRequest.getServiceCapabilities().isRon());
        notaryCapability.setMobile(capabilityRequest.getServiceCapabilities().isMobileNotary());
        notaryCapability.setLoanSigning(capabilityRequest.getServiceCapabilities().isLoanSigning());
        notaryCapability.setApostilleRelatedSupport(capabilityRequest.getServiceCapabilities().isApostilleSupport());
        notaryCapability.setMaxDistance((float)capabilityRequest.getMaxTravelDistance());
    }

    private List<DayOfWeek>  getWorkingDays(int workingDaysPerWeek, FixedDayOffEnum offDay){
        return Arrays.stream(DayOfWeek.values())
                .filter(day -> offDay == null || !day.name().equals(offDay.name()))
                .limit(workingDaysPerWeek)
                .toList();
    }

}
