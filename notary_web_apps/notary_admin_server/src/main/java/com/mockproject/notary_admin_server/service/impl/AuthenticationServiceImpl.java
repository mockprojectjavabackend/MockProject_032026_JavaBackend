package com.mockproject.notary_admin_server.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mockproject.notary_admin_server.dto.request.*;
import com.mockproject.notary_admin_server.dto.response.AuthenticationResponse;
import com.mockproject.notary_admin_server.exception.AppException;
import com.mockproject.notary_admin_server.exception.errorCode.JwtErrorCode;
import com.mockproject.notary_admin_server.exception.errorCode.PasswordErrorCode;
import com.mockproject.notary_admin_server.exception.errorCode.TokenErrorCode;
import com.mockproject.notary_admin_server.exception.errorCode.UserErrorCode;
import com.mockproject.notary_admin_server.repository.UserInvitationTokenRepository;
import com.mockproject.notary_admin_server.repository.UserRepository;
import com.mockproject.notary_admin_server.service.AuthenticationService;
import com.mockproject.notary_admin_server.service.JwtService;
import com.mockproject.notary_common.constant.UserStatus;
import com.mockproject.notary_common.entity.User;
import com.mockproject.notary_common.entity.UserInvitationToken;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {

    PasswordEncoder passwordEncoder;

    UserRepository userRepository;

    JwtService jwtService;

    UserInvitationTokenRepository invitationTokenRepository;

    @Override
    @Transactional
    public AuthenticationResponse login(AuthenticationRequest request) {
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));


        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if (!isAuthenticated) {
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

    @Override
    @Transactional
    public AuthenticationResponse refreshToken(String refreshToken) {
        SignedJWT signedJWT = jwtService.verifyRefreshToken(refreshToken);

        try {
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();

            String email = jwtClaimsSet.getSubject();

            User user =
                    userRepository.findByEmail(email).orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

            if (user.getStatus() != UserStatus.ACTIVE) {
                throw new AppException(UserErrorCode.USER_STATUS_INVALID);
            }

            String familyId = jwtService.getTokenFamilyId(refreshToken);

            jwtService.revokeRefreshToken(refreshToken);

            String newAccessToken = jwtService.createAccessToken(user);

            String newRefreshToken = jwtService.createRefreshToken(user, familyId);

            log.info("Refresh token successfully for user: email={}", email);

            return AuthenticationResponse.builder()
                    .token(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .authenticated(true)
                    .build();
        } catch (Exception e) {
            log.error("Error refreshing token: {}", e.getMessage());
            throw new AppException(JwtErrorCode.REFRESH_TOKEN_INVALID);
        }
    }

    @Override
    @Transactional
    public void logout(String refreshToken, String accessToken) {
        jwtService.verifyRefreshToken(refreshToken);
        jwtService.revokeRefreshToken(refreshToken);

        if (accessToken != null && !accessToken.isBlank()) {
            jwtService.invalidateAccessToken(accessToken);
        }

        log.info("Refresh token revoked successfully");
    }

    @Override
    @Transactional
    public void logoutAll(UUID userId) {
        jwtService.revokeAllTokensOfUser(userId);
        log.info("All tokens revoked for user: {}", userId);
    }

    @Override
    @Transactional
    public void setPassword(SetPasswordRequest request) {
        UserInvitationToken invitationToken = invitationTokenRepository
                .findByToken(request.getToken())
                .orElseThrow(() -> new AppException(TokenErrorCode.TOKEN_INVALID));

        if (Boolean.TRUE.equals(invitationToken.getUsed())) {
            throw new AppException(TokenErrorCode.TOKEN_ALREADY_USED);
        }

        if (invitationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AppException(TokenErrorCode.TOKEN_EXPIRED);
        }

        User user = invitationToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);

        invitationToken.setUsed(true);
        invitationTokenRepository.save(invitationToken);
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

