package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_common.entity.User;
import com.nimbusds.jwt.SignedJWT;

import java.util.UUID;

public interface JwtService {
    String createAccessToken(User user);

    String createRefreshToken(User user, String familyId);

    SignedJWT verifyAccessToken(String token);

    SignedJWT verifyRefreshToken(String refreshToken);

    void revokeRefreshToken(String refreshToken);

    void revokeAllTokensOfUser(UUID userId);

    String getTokenFamilyId(String refreshToken);
}
