package com.mockproject.notary_admin_server.mapper;

import com.mockproject.notary_admin_server.dto.response.capability.AvailabilityDTO;
import com.mockproject.notary_common.entity.notary.NotaryAvailability;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface NotaryAvailabilityMapper {

//    @Mapping(source = "workingDaysPerWeek", target = "workingDaysPerWeek")
//    @Mapping(source = "startTime", target = "startTime")
//    @Mapping(source = "endTime", target = "endTime")
//    @Mapping(source = "fixedDayOff", target = "fixedDayOff")
    AvailabilityDTO toAvailabilityDTO(NotaryAvailability notaryAvailability);
}
