package com.mockproject.notary_admin_server.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.request.AdminCreateUserRequest;
import com.mockproject.notary_admin_server.dto.response.UserResponse;
import com.mockproject.notary_admin_server.service.AdminUserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * AdminUserController
 *
 * @version 1.0
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 03-04-2026      VanTien     create
 */
@RestController
@Slf4j(topic = "ADMIN-USER-CONTROLLER")
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserController {
    AdminUserService adminUserService;

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<UserResponse>> createUser(
            @RequestBody @Valid AdminCreateUserRequest request) {
        UserResponse response = adminUserService.createUser(request);

        return ResponseEntity.ok(ApiSuccessResponse.ok(response));
    }
}
