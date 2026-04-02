package com.mockproject.notary_admin_server.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mockproject.notary_admin_server.exception.AppException;
import com.mockproject.notary_admin_server.exception.errorCode.AuthErrorCode;
import com.mockproject.notary_admin_server.exception.errorCode.JwtErrorCode;
import com.mockproject.notary_admin_server.exception.errorCode.UserErrorCode;
import com.mockproject.notary_admin_server.repository.RefreshTokenRepository;
import com.mockproject.notary_admin_server.repository.UserRepository;
import com.mockproject.notary_admin_server.service.JwtService;
import com.mockproject.notary_common.constant.PredefinedRole;
import com.mockproject.notary_common.constant.RevocationReason;
import com.mockproject.notary_common.constant.TokenType;
import com.mockproject.notary_common.constant.UserStatus;
import com.mockproject.notary_common.entity.RefreshToken;
import com.mockproject.notary_common.entity.Role;
import com.mockproject.notary_common.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "JWT-SERVICE")
public class JwtServiceImpl implements JwtService {
    private static final String SCOPE_CLAIM = "scope";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String TOKEN_TYPE_CLAIM = "token_type";
    private static final String USER_ID_CLAIM = "user_id";
    private static final String FAMILY_ID_CLAIM = "family_id";

    @NonFinal
    @Value("${jwt.signerKey}")
    private String signerKey;

    @NonFinal
    @Value("${jwt.valid-duration}")
    private long validDuration;

    @NonFinal
    @Value("${jwt.issuer}")
    private String issuer;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    private long refreshableDuration;

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public String createAccessToken(User user) {
        validateUser(user);
        log.debug("Creating access token for user with email: {}", user.getEmail());
        return createToken(user, TokenType.ACCESS, validDuration, null);
    }

    @Override
    public String createRefreshToken(User user, String familyId) {
        validateUser(user);

        log.debug("Creating refresh token for user with email: {}", user.getEmail());

        String tokenFamilyId = familyId != null ? familyId : UUID.randomUUID().toString();

        String token = createToken(user, TokenType.REFRESH, refreshableDuration, tokenFamilyId);

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            RefreshToken refreshToken = RefreshToken.builder()
                    .tokenHash(hashToken(token))
                    .jti(claimsSet.getJWTID())
                    .familyId(tokenFamilyId)
                    .user(user)
                    .issuedAt(claimsSet.getIssueTime().toInstant())
                    .expiresAt(claimsSet.getExpirationTime().toInstant())
                    .revoked(false)
                    .build();

            refreshTokenRepository.save(refreshToken);
            log.debug("Successfully created refresh token for user with email: {}", user.getEmail());
        } catch (ParseException e) {
            log.error(
                    "Error parsing refresh token for user with email: {}, error: {}", user.getEmail(), e.getMessage());
            throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
        }

        return token;
    }

    @Override
    public SignedJWT verifyAccessToken(String token) {
        return verifyToken(token, TokenType.ACCESS);
    }

    @Override
    public SignedJWT verifyRefreshToken(String refreshToken) {
        SignedJWT signedJWT = verifyToken(refreshToken, TokenType.REFRESH);

        try {
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            String jti = claimsSet.getJWTID();

            Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByJti(jti);

            if (optionalRefreshToken.isEmpty()) {
                log.warn("Refresh token not found in database for jti: {}", jti);
                throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
            }

            RefreshToken storedRefreshToken = optionalRefreshToken.get();

            if (storedRefreshToken.isRevoked()) {
                String familyId = storedRefreshToken.getFamilyId();

                log.warn(
                        "Refresh token is revoked for jti: {}, revoking all tokens in the same family with familyId: {}",
                        jti,
                        familyId);

                int revokedCount = refreshTokenRepository.revokeAllByFamilyId(
                        familyId, Instant.now(), RevocationReason.SECURITY_BREACH);

                log.warn(
                        "Revoked {} refresh tokens in the same family with familyId: {} due to security breach",
                        revokedCount,
                        familyId);

                throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
            }

            if (!optionalRefreshToken.get().isValid()) {
                log.warn("Refresh token is invalid for jti: {}", jti);
                throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
            }

            return signedJWT;

        } catch (ParseException e) {
            log.error("Error parsing refresh token during verification, error: {}", e.getMessage());
            throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
        }
    }

    @Override
    public void revokeRefreshToken(String refreshToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(refreshToken);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            String jti = claimsSet.getJWTID();

            if (jti == null) {
                log.warn("Token missing jti or expiration time");
                throw new AppException(JwtErrorCode.TOKEN_INVALID);
            }

            Optional<RefreshToken> storedTokenOpt = refreshTokenRepository.findByJti(jti);

            if (storedTokenOpt.isPresent()) {
                RefreshToken storedToken = storedTokenOpt.get();
                if (!storedToken.isRevoked()) {
                    storedToken.revoke(RevocationReason.TOKEN_REFRESH);
                    refreshTokenRepository.save(storedToken);
                    log.info("Token revoked successfully: {}", jti);
                } else {
                    log.debug("Token already revoked: {}", jti);
                }
            } else {
                log.warn("Token not found in database: {}", jti);
            }

        } catch (ParseException e) {
            log.error("failed to parse JWT token for revocation", e);
            throw new AppException(JwtErrorCode.TOKEN_INVALID);
        }
    }

    @Override
    public void revokeAllTokensOfUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

        userRepository.save(user);

        // Revoke all refresh tokens in database

        log.info("Revoked {} refresh tokens for user {}",
                refreshTokenRepository.revokeAllByUserId(userId, Instant.now(), RevocationReason.USER_LOGOUT_ALL),
                user.getEmail());
    }

    @Override
    public String getTokenFamilyId(String refreshToken) {
        SignedJWT signedJWT = verifyToken(refreshToken, TokenType.REFRESH);
        try {
            return signedJWT.getJWTClaimsSet().getStringClaim(FAMILY_ID_CLAIM);
        } catch (ParseException e) {
            log.error("Failed to parse refresh token claims to get family ID", e);
            throw new AppException(JwtErrorCode.TOKEN_INVALID);
        }
    }

    private String createToken(User user, TokenType tokenType, long duration, String familyId) {
        try {
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

            JWTClaimsSet jwtClaimsSet = buildJwtClaimsSet(user, tokenType, duration, familyId);

            SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);

            signedJWT.sign(new MACSigner(getSignerKeyBytes()));

            String token = signedJWT.serialize();

            log.debug("Successfully created {} token for email: {}", tokenType, user.getEmail());

            return token;
        } catch (JOSEException e) {
            log.error("Error creating {} token for email: {}, error: {}", tokenType, user.getEmail(), e.getMessage());
            throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
        }
    }

    private JWTClaimsSet buildJwtClaimsSet(User user, TokenType tokenType, long duration, String familyId) {
        Instant now = Instant.now();

        Date issueTime = Date.from(now);
        Date expirationTime = Date.from(now.plus(duration, ChronoUnit.SECONDS));

        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .issuer(issuer)
                .subject(user.getEmail())
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .jwtID(UUID.randomUUID().toString())
                .claim(USER_ID_CLAIM, user.getId())
                .claim(TOKEN_TYPE_CLAIM, tokenType.name())
                .claim(SCOPE_CLAIM, buildRoles(user.getRoles()));
        if (tokenType == TokenType.REFRESH) {
            builder.claim(FAMILY_ID_CLAIM, familyId);
        }

        return builder.build();
    }

    List<String> buildRoles(Set<Role> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return List.of();
        }

        return roles.stream()
                .filter(Objects::nonNull)
                .map(Role::getRoleName)
                .filter(Objects::nonNull)
                .map(PredefinedRole::name)
                .map(roleName -> ROLE_PREFIX + roleName)
                .distinct()
                .toList();
    }

    private byte[] getSignerKeyBytes() {
        if (signerKey == null || signerKey.isEmpty()) {
            throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
        }
        return signerKey.getBytes(StandardCharsets.UTF_8);
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
        }

        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
        }

        if (user.getDeletedAt() != null) {
            throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
        }
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 algorithm not found for token hashing", e);
            throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
        }
    }

    private SignedJWT verifyToken(String token, TokenType tokenType) {
        if (token == null || token.isBlank()) {
            log.warn("Token is null or blank during verification");
            throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
        }

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWSVerifier verifier = new MACVerifier(getSignerKeyBytes());

            if (!signedJWT.verify(verifier)) {
                log.warn("Token signature verification failed for token: {}", token);
                throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
            }

            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            Date expirationTime = claimsSet.getExpirationTime();

            if (expirationTime == null || expirationTime.before(new Date())) {
                log.warn("Token is expired during verification for token: {}", token);
                throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
            }

            String tokenIssuer = claimsSet.getIssuer();
            if (tokenIssuer == null || !tokenIssuer.equals(issuer)) {
                log.warn("Token issuer mismatch during verification. Expected: {}, Found: {}", issuer, tokenIssuer);
                throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
            }

            if (tokenType == null) {
                log.warn("Token type is null during verification for token: {}", token);
                throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
            }

            String tokenTypeInClaim = claimsSet.getStringClaim(TOKEN_TYPE_CLAIM);
            if (tokenTypeInClaim == null || tokenTypeInClaim.isBlank()) {
                log.warn(
                        "Token type claim is missing or blank during verification. Expected: {}",
                        tokenType.name());
                throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
            }

            if (!tokenType.name().equals(tokenTypeInClaim)) {
                log.warn(
                        "Token type claim does not match expected token type during verification. Expected: {}, Found: {}",
                        tokenType.name(),
                        tokenTypeInClaim);
                throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
            }

            log.debug("Token verified successfully for token: {}", token);
            return signedJWT;

        } catch (ParseException | JOSEException e) {
            log.error("Error parsing or verifying JWT token", e);
            throw new AppException(AuthErrorCode.AUTHENTICATION_FAILED);
        }
    }
}
