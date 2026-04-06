package com.mockproject.notary_admin_server.controller;

import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.request.AuthenticationRequest;
import com.mockproject.notary_admin_server.dto.request.LogoutRequest;
import com.mockproject.notary_admin_server.dto.request.RefreshTokenRequest;
import com.mockproject.notary_admin_server.dto.request.SetPasswordRequest;
import com.mockproject.notary_admin_server.dto.response.AuthenticationResponse;
import com.mockproject.notary_admin_server.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * AuthenticationController
 *
 * @version 1.0
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 01-04-2026      VanTien     create
 * 03-04-2026      VanTien     edit
 * 04-04-2026      VanTien     edit
 */
@RestController
@Slf4j(topic = "AUTHENTICATION-CONTROLLER")
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping(value = "token", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<AuthenticationResponse>> login(
            @Valid @RequestBody AuthenticationRequest request) {
        log.info("Login request: email={}", request.getEmail());
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok(ApiSuccessResponse.ok(response));
    }

    @PostMapping(value = "set-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Void>> setPassword(@Valid @RequestBody SetPasswordRequest request) {
        log.info(
                "Set-password request received (token prefix={}...)",
                request.getToken() != null && request.getToken().length() > 8
                        ? request.getToken().substring(0, 8)
                        : "?");
        authenticationService.setPassword(request);
        return ResponseEntity.ok(ApiSuccessResponse.ok(null));
    }

    @PostMapping(value = "refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<AuthenticationResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        log.info("Refresh token request received");
        AuthenticationResponse response = authenticationService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(ApiSuccessResponse.ok(response));
    }

    @PostMapping(value = "logout", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Void>> logout(
            @Valid @RequestBody LogoutRequest request, HttpServletRequest httpRequest) {
        log.info("Logout request received");

        String accessToken = extractBearerToken(httpRequest);

        authenticationService.logout(request.getRefreshToken(), accessToken);
        return ResponseEntity.ok(ApiSuccessResponse.ok(null));
    }

    @PostMapping("logout-all")
    public ResponseEntity<ApiSuccessResponse<Void>> logoutAll(@AuthenticationPrincipal Jwt jwt) {
        log.info("Logout-all request received");
        UUID userId = UUID.fromString(jwt.getClaimAsString("user_id"));
        authenticationService.logoutAll(userId);
        return ResponseEntity.ok(ApiSuccessResponse.ok(null));
    }

    private String extractBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}

