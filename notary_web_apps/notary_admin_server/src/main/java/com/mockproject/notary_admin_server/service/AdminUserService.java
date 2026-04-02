package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_admin_server.dto.request.AdminCreateUserRequest;
import com.mockproject.notary_admin_server.dto.response.UserResponse;

public interface AdminUserService {
    UserResponse createUser(AdminCreateUserRequest request);
}
