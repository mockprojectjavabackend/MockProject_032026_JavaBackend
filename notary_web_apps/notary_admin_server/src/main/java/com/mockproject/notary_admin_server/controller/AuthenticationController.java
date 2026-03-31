package com.mockproject.notary_admin_server.controller;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.request.AuthenticationRequest;
import com.mockproject.notary_admin_server.dto.response.AuthenticationResponse;
import com.mockproject.notary_admin_server.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j(topic = "AUTHENTICATION-CONTROLLER")
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping(value = "token", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<AuthenticationResponse>> login(
            @Valid @RequestBody AuthenticationRequest request
    ){
        log.info("Authenticate request: email={}", request.getEmail());

        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok(ApiSuccessResponse.ok(response));
    }
}
