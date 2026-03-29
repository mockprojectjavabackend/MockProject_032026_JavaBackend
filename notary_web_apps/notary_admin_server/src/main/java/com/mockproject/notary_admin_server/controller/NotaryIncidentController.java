package com.mockproject.notary_admin_server.controller;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.response.IncidentResponse;
import com.mockproject.notary_admin_server.service.NotaryIncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notaries")
@RequiredArgsConstructor
public class NotaryIncidentController {

    private final NotaryIncidentService notaryIncidentService;

    @GetMapping("/{notaryId}/incidents")
    public ApiSuccessResponse<List<IncidentResponse>> getRecentIncidents(
            @PathVariable UUID notaryId,
            @RequestParam(defaultValue = "5") int limit) {

        List<IncidentResponse> data = notaryIncidentService.getRecentIncidents(notaryId, limit);
        return ApiSuccessResponse.success(200, "Get recent incidents successfully", data);
    }
}
