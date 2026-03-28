package com.mockproject.notary_admin_server.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mockproject.notary_admin_server.dto.PaginationResponse;
import com.mockproject.notary_admin_server.dto.request.CommissionQuery;
import com.mockproject.notary_admin_server.dto.response.CommissionDetailResponse;
import com.mockproject.notary_admin_server.dto.response.CommissionListResponse;
import com.mockproject.notary_admin_server.service.NotaryCommissionService;
import com.mockproject.notary_common.entity.notary.NotaryCommission;
import com.turkraft.springfilter.boot.Filter;

@RestController
@RequestMapping("/api/notaries")
public class NotaryCommissionController {
    private final NotaryCommissionService notaryCommissionService;

    public NotaryCommissionController(NotaryCommissionService notaryCommissionService) {
        this.notaryCommissionService = notaryCommissionService;
    }

    @GetMapping("{id}/commissions")
    public ResponseEntity<PaginationResponse<List<CommissionListResponse>>> getAllNotaryCommissions(
            @PathVariable("id") UUID notaryId, CommissionQuery query) {
        PaginationResponse<List<CommissionListResponse>> listCommissions = this.notaryCommissionService
                .fetchAllNotaryCommissions(notaryId, query);
        return ResponseEntity.ok().body(listCommissions);
    }

    @GetMapping("{id}/commissions/{commissionId}")
    public ResponseEntity<CommissionDetailResponse> getById(
            @PathVariable("id") UUID notaryId,
            @PathVariable("commissionId") UUID commissionId) {
        return ResponseEntity.ok(
                notaryCommissionService.getCommissionById(notaryId, commissionId));
    }
}
