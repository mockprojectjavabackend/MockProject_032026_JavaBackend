package com.mockproject.notary_admin_server.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.request.AdminInviteNotaryRequest;
import com.mockproject.notary_admin_server.dto.response.NotaryInviteResponse;
import com.mockproject.notary_admin_server.service.AdminNotaryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j(topic = "ADMIN-NOTARY-CONTROLLER")
@RequiredArgsConstructor
@RequestMapping("/admin/notaries")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminNotaryController {
    AdminNotaryService adminNotaryService;

    @PostMapping("/invite")
    public ResponseEntity<ApiSuccessResponse<NotaryInviteResponse>> inviteNotary(
            @RequestBody @Valid AdminInviteNotaryRequest request) {
        NotaryInviteResponse response = adminNotaryService.inviteNotary(request);
        return ResponseEntity.ok(ApiSuccessResponse.ok(response));
    }
}
