package com.mockproject.notary_admin_server.mapper;

import com.mockproject.notary_admin_server.dto.response.ServiceCapabilitiesDTO;
import com.mockproject.notary_common.entity.notary.NotaryCapability;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface NotaryCapabilityMapper {

    ServiceCapabilitiesDTO toServiceCapabilitiesDTO(NotaryCapability notaryCapability);
}

