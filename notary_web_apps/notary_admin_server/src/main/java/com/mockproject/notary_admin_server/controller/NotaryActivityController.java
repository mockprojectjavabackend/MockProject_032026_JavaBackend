package com.mockproject.notary_admin_server.controller;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.response.RecentActivityResponse;
import com.mockproject.notary_admin_server.service.NotaryActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notaries")
@RequiredArgsConstructor
public class NotaryActivityController {

    private final NotaryActivityService notaryActivityService;

    @GetMapping("/{notaryId}/activities")
    public ApiSuccessResponse<List<RecentActivityResponse>> getRecentActivities(
            @PathVariable UUID notaryId,
            @RequestParam(defaultValue = "5") int limit) {

        List<RecentActivityResponse> data = notaryActivityService.getRecentActivities(notaryId, limit);
        return ApiSuccessResponse.ok(data);
    }
}