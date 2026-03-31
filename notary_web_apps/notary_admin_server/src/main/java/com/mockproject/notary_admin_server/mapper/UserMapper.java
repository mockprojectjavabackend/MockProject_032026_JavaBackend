package com.mockproject.notary_admin_server.mapper;

import com.mockproject.notary_admin_server.dto.request.UserRegisterRequest;
import com.mockproject.notary_admin_server.dto.response.UserResponse;
import com.mockproject.notary_common.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegisterRequest request);

    UserResponse toUserResponse(User user);
}
