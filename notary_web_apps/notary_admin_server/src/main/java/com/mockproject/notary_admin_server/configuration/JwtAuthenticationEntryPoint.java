package com.mockproject.notary_admin_server.configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.mockproject.notary_admin_server.dto.ApiErrorResponse;
import com.mockproject.notary_admin_server.exception.ErrorCode;
import com.mockproject.notary_admin_server.exception.errorCode.AuthErrorCode;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

/**
 * JwtAuthenticationEntryPoint
 *
 * @version 1.0
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      VanTien     create
 */

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        if (response.isCommitted()) {
            return;
        }

        ErrorCode errorCode = AuthErrorCode.AUTHENTICATION_FAILED;

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ApiErrorResponse apiResponse = ApiErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .path(request.getRequestURI())
                .errors(java.util.Map.of(errorCode.getCode(), errorCode.getMessage()))
                .timestamp(java.time.Instant.now().toString())
                .build();
        log.warn(
                "Unauthorized: method={} path={} ip={} reason={}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                authException.getMessage());

        objectMapper.writeValue(response.getWriter(), apiResponse);

        response.flushBuffer();
    }
}
