package com.mockproject.notary_admin_server.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.PaginationResponse;
import com.mockproject.notary_admin_server.dto.request.CommissionQuery;
import com.mockproject.notary_admin_server.dto.request.CreateNotaryCommissionRequest;
import com.mockproject.notary_admin_server.dto.request.UpdateNotaryCommissionRequest;
import com.mockproject.notary_admin_server.dto.response.CommissionDetailResponse;
import com.mockproject.notary_admin_server.dto.response.CommissionListResponse;
import com.mockproject.notary_admin_server.service.NotaryCommissionService;
import com.mockproject.notary_common.entity.notary.NotaryCommission;

/**
 * NotaryCommissionController
 *
 * @version 1.0
 * @date 29-03-2026
 *       <p>
 *       Modification Logs:
 *       DATE AUTHOR DESCRIPTION
 *       -----------------------------------------------
 *       29-03-2026 HuyenThuong Crud notary commission
 */
@RestController
@RequestMapping("/api/notaries")
public class NotaryCommissionController {
    private final NotaryCommissionService notaryCommissionService;

    public NotaryCommissionController(NotaryCommissionService notaryCommissionService) {
        this.notaryCommissionService = notaryCommissionService;
    }

    @GetMapping("{id}/commissions")
    public ResponseEntity<ApiSuccessResponse<PaginationResponse<List<CommissionListResponse>>>> getAllNotaryCommissions(
            @PathVariable("id") UUID notaryId, CommissionQuery query) {
        PaginationResponse<List<CommissionListResponse>> listCommissions = this.notaryCommissionService
                .fetchAllNotaryCommissions(notaryId, query);
        return ResponseEntity.ok().body(ApiSuccessResponse.ok(listCommissions));
    }

    @GetMapping("{id}/commissions/{commissionId}")
    public ResponseEntity<ApiSuccessResponse<CommissionDetailResponse>> getById(
            @PathVariable("id") UUID notaryId,
            @PathVariable("commissionId") UUID commissionId) {
        return ResponseEntity.ok(
                ApiSuccessResponse.ok(notaryCommissionService.getCommissionById(notaryId, commissionId)));
    }

    @PostMapping("{id}/commissions")
    public ResponseEntity<ApiSuccessResponse<CommissionDetailResponse>> create(
            @PathVariable("id") UUID notaryId,
            @RequestBody CreateNotaryCommissionRequest req) {
        return ResponseEntity.ok(ApiSuccessResponse.created(notaryCommissionService.createCommission(notaryId, req)));
    }

    @PutMapping("{notaryId}/commissions/{commissionId}")
    public ResponseEntity<ApiSuccessResponse<CommissionDetailResponse>> update(
            @PathVariable UUID notaryId,
            @PathVariable UUID commissionId,
            @RequestBody UpdateNotaryCommissionRequest req) {
        return ResponseEntity.ok(
                ApiSuccessResponse.ok(notaryCommissionService.updateCommission(notaryId, commissionId, req)));
    }

    @DeleteMapping("{notaryId}/commissions/{commissionId}")
    public ResponseEntity<ApiSuccessResponse<Void>> delete(
            @PathVariable UUID notaryId,
            @PathVariable UUID commissionId) {
        notaryCommissionService.deleteCommission(notaryId, commissionId);
        return ResponseEntity.ok(ApiSuccessResponse.deleted());
    }
}
