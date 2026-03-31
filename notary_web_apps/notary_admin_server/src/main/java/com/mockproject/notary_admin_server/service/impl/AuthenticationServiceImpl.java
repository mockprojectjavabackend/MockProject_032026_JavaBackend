package com.mockproject.notary_admin_server.service.impl;

import com.mockproject.notary_admin_server.dto.request.AuthenticationRequest;
import com.mockproject.notary_admin_server.dto.response.AuthenticationResponse;
import com.mockproject.notary_admin_server.exception.AppException;
import com.mockproject.notary_admin_server.exception.errorCode.PasswordErrorCode;
import com.mockproject.notary_admin_server.exception.errorCode.UserErrorCode;
import com.mockproject.notary_admin_server.mapper.RoleMapper;
import com.mockproject.notary_admin_server.mapper.UserMapper;
import com.mockproject.notary_admin_server.repository.UserRepository;
import com.mockproject.notary_admin_server.service.AuthenticationService;
import com.mockproject.notary_admin_server.service.JwtService;
import com.mockproject.notary_common.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {
    PasswordEncoder passwordEncoder;

    UserMapper userMapper;

    RoleMapper roleMapper;

    UserRepository userRepository;

    JwtService jwtService;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        User user =userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new AppException(UserErrorCode.USER_NOT_FOUND));

        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if(!isAuthenticated) {
            throw new AppException(PasswordErrorCode.PASSWORD_INCORRECT);
        }

        validateUserStatusForLogin(user);

        String accessToken = jwtService.createAccessToken(user);

        String refreshToken = jwtService.createRefreshToken(user, null);

        log.info("User logged in successfully: email={}", request.getEmail());

        return AuthenticationResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }

    private void validateUserStatusForLogin(User user) {
        if (user.getStatus() == null) {
            throw new AppException(UserErrorCode.USER_STATUS_INVALID);
        }

        switch (user.getStatus()) {
            case ACTIVE:
                return;

            case INACTIVE:
                throw new AppException(UserErrorCode.USER_ACCOUNT_INACTIVE);

            case SUSPENDED:
                throw new AppException(UserErrorCode.USER_ACCOUNT_SUSPENDED);

            case BLOCKED:
                throw new AppException(UserErrorCode.USER_ACCOUNT_BLOCKED);

            case DELETED:
                throw new AppException(UserErrorCode.USER_ACCOUNT_DELETED);

            default:
                throw new AppException(UserErrorCode.USER_STATUS_INVALID);
        }
    }
}
