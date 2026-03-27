package com.mockproject.notary_admin_server.controller;

import java.util.UUID;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryDetailResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryOverviewResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryStatusResponse;
import com.mockproject.notary_admin_server.service.NotaryOverviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notaries")
public class NotaryOverviewController {

    private final NotaryOverviewService notaryOverviewService;

    public NotaryOverviewController(NotaryOverviewService notaryOverviewService) {
        this.notaryOverviewService = notaryOverviewService;
    }

    @GetMapping("/{notary_id}/overview")
    public ResponseEntity<ApiSuccessResponse<NotaryOverviewResponse>> getNotaryOverview(@PathVariable UUID notary_id) {
        return ResponseEntity.ok(ApiSuccessResponse.ok(notaryOverviewService.getNotaryOverview(notary_id)));
    }

    @PatchMapping("/admin/{notary_id}/deactivate")
    public ResponseEntity<ApiSuccessResponse<NotaryStatusResponse>> deactivateNotary(@PathVariable UUID notary_id){
        return ResponseEntity.ok(ApiSuccessResponse.ok(notaryOverviewService.deactivateNotary(notary_id)));
    }

    @GetMapping("/{notary_id}")
    public ResponseEntity<ApiSuccessResponse<NotaryDetailResponse>> getNotaryDetail(@PathVariable UUID notary_id) {
        return ResponseEntity.ok(ApiSuccessResponse.ok(notaryOverviewService.getNotaryDetail(notary_id)));
    }
}
