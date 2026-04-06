package com.mockproject.notary_admin_server.mapper;

import com.mockproject.notary_admin_server.dto.response.capability.AvailabilityDTO;
import com.mockproject.notary_common.entity.notary.NotaryAvailability;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
public interface NotaryAvailabilityMapper {

    AvailabilityDTO toAvailabilityDTO(NotaryAvailability notaryAvailability);
}
