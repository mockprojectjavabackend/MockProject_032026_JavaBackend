package com.mockproject.notary_admin_server.service;

import java.util.UUID;

import com.mockproject.notary_admin_server.dto.request.*;
import com.mockproject.notary_admin_server.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);

    AuthenticationResponse refreshToken(String refreshToken);

    void logout(String refreshToken, String accessToken);

    void logoutAll(UUID userId);

    void setPassword(SetPasswordRequest request);
}

