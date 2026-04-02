package com.mockproject.notary_admin_server.configuration;

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.extern.slf4j.Slf4j;

/**
 * CustomJwtDecoder
 *
 * @version 1.1
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      VanTien     create
 * 03-04-2026      VanTien     add HMAC signature & expiry verification
 */

@Component
@Slf4j(topic = "CUSTOM-JWT-DECODER")
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            // 1. Verify HMAC-SHA256 signature
            MACVerifier verifier = new MACVerifier(signerKey.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            if (!signedJWT.verify(verifier)) {
                log.warn("JWT signature verification failed");
                throw new JwtException("Chữ ký token không hợp lệ");
            }

            // 2. Verify token not expired
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            Date expiration = claimsSet.getExpirationTime();
            if (expiration == null || expiration.before(new Date())) {
                log.warn("JWT token is expired");
                throw new JwtException("Token đã hết hạn");
            }

            return new Jwt(
                    token,
                    claimsSet.getIssueTime().toInstant(),
                    claimsSet.getExpirationTime().toInstant(),
                    signedJWT.getHeader().toJSONObject(),
                    claimsSet.getClaims());

        } catch (ParseException e) {
            log.error("Failed to parse JWT token", e);
            throw new JwtException("Token không hợp lệ");
        } catch (JOSEException e) {
            log.error("JOSE exception during JWT verification", e);
            throw new JwtException("Xác thực token thất bại");
        }
    }
}
