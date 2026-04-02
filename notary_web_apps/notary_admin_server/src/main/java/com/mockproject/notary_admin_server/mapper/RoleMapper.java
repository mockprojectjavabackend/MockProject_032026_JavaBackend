package com.mockproject.notary_admin_server.mapper;

import org.mapstruct.Mapper;

import com.mockproject.notary_admin_server.dto.response.RoleResponse;
import com.mockproject.notary_common.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toRoleResponse(Role role);
}
