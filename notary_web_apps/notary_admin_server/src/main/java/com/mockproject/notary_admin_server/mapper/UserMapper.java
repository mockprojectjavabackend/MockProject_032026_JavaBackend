package com.mockproject.notary_admin_server.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.mockproject.notary_admin_server.dto.request.AdminCreateUserRequest;
import com.mockproject.notary_admin_server.dto.response.UserResponse;
import com.mockproject.notary_common.constant.PredefinedRole;
import com.mockproject.notary_common.entity.Role;
import com.mockproject.notary_common.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "notary", ignore = true)
    User toUser(AdminCreateUserRequest request);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToPredefinedRoles")
    UserResponse toUserResponse(User user);

    @Named("mapRolesToPredefinedRoles")
    default Set<PredefinedRole> mapRolesToPredefinedRoles(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());
    }
}
