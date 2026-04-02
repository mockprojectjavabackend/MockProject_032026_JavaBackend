package com.mockproject.notary_admin_server.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mockproject.notary_admin_server.dto.request.AdminCreateUserRequest;
import com.mockproject.notary_admin_server.dto.response.UserResponse;
import com.mockproject.notary_admin_server.exception.AppException;
import com.mockproject.notary_admin_server.exception.errorCode.RoleErrorCode;
import com.mockproject.notary_admin_server.exception.errorCode.UserErrorCode;
import com.mockproject.notary_admin_server.mapper.UserMapper;
import com.mockproject.notary_admin_server.repository.RoleRepository;
import com.mockproject.notary_admin_server.repository.UserRepository;
import com.mockproject.notary_admin_server.service.AdminUserService;
import com.mockproject.notary_common.constant.PredefinedRole;
import com.mockproject.notary_common.constant.UserStatus;
import com.mockproject.notary_common.entity.Role;
import com.mockproject.notary_common.entity.User;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j(topic = "ADMIN-USER-SERVICE")
public class AdminUserServiceImpl implements AdminUserService {

    UserRepository userRepository;

    UserMapper userMapper;

    RoleRepository roleRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserResponse createUser(AdminCreateUserRequest request) {
        String email = request.getEmail().toLowerCase();

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }

        validateRole(request.getRole());

        User user = userMapper.toUser(request);

        // Set auth
        user.setStatus(UserStatus.INACTIVE);

        Role role = roleRepository
                .findByRoleName(request.getRole())
                .orElseThrow(() -> new AppException(RoleErrorCode.ROLE_NOT_FOUND));

        user.getRoles().add(role);

        User saved = userRepository.save(user);

        log.info("Admin created new user email: {}", saved.getEmail());

        return userMapper.toUserResponse(saved);
    }

    private void validateRole(PredefinedRole role) {
        if (role != PredefinedRole.NOTARY && role != PredefinedRole.DISPATCHER) {
            throw new AppException(RoleErrorCode.ROLE_NOT_FOUND);
        }
    }
}
