package com.mockproject.notary_admin_server.service;

import java.util.UUID;

import com.mockproject.notary_common.entity.User;
import com.nimbusds.jwt.SignedJWT;

public interface JwtService {
    String createAccessToken(User user);

    String createRefreshToken(User user, String familyId);

    SignedJWT verifyRefreshToken(String refreshToken);

    void revokeRefreshToken(String refreshToken);

    void revokeAllTokensOfUser(UUID userId);

    String getTokenFamilyId(String refreshToken);

    void invalidateAccessToken(String accessToken);
}

