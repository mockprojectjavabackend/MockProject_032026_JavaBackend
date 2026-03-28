package com.mockproject.notary_admin_server.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mockproject.notary_admin_server.dto.PaginationResponse;
import com.mockproject.notary_admin_server.dto.request.CommissionQuery;
import com.mockproject.notary_admin_server.dto.response.CommissionDetailResponse;
import com.mockproject.notary_admin_server.dto.response.CommissionListResponse;
import com.mockproject.notary_admin_server.exception.ForbiddenException;
import com.mockproject.notary_admin_server.exception.NotFoundException;
import com.mockproject.notary_admin_server.repository.NotaryCommissionRepository;
import com.mockproject.notary_admin_server.service.NotaryCommissionService;
import com.mockproject.notary_common.entity.notary.NotaryCommission;

@Service
public class NotaryCommissionServiceImpl implements NotaryCommissionService {
    private final NotaryCommissionRepository notaryCommissionRepository;

    public NotaryCommissionServiceImpl(NotaryCommissionRepository notaryCommissionRepository) {
        this.notaryCommissionRepository = notaryCommissionRepository;
    }

    public PaginationResponse<List<CommissionListResponse>> fetchAllNotaryCommissions(UUID notaryId,
            CommissionQuery q) {
        Specification<NotaryCommission> spec = Specification.where(
                (root, query, cb) -> cb.equal(root.get("notary").get("id"), notaryId));

        if (q.getStatus() != null && !q.getStatus().equalsIgnoreCase("All Statuses")) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), q.getStatus().toUpperCase()));
        }

        if (q.getState() != null && !q.getState().equalsIgnoreCase("All States")) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("state").get("stateName"), q.getState()));
        }

        if (q.getSearch() != null && !q.getSearch().isBlank()) {
            String keyword = "%" + q.getSearch().toLowerCase() + "%";

            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("commissionNumber")), keyword));
        }

        if (q.getExpirationDate() != null) {
            LocalDate now = LocalDate.now();
            if ("7 days".equalsIgnoreCase(q.getExpirationDate())) {
                spec = spec.and((root, query, cb) -> cb.between(root.get("expirationDate"), now, now.plusDays(7)));
            }

            else if ("30 days".equalsIgnoreCase(q.getExpirationDate())) {
                spec = spec.and((root, query, cb) -> cb.between(root.get("expirationDate"), now, now.plusDays(30)));
            }
        }
        Pageable pageable = PageRequest.of(q.getPage() - 1, q.getLimit());

        Page<NotaryCommission> result = notaryCommissionRepository.findAll(spec, pageable);

        Page<CommissionListResponse> dtoPage = result
                .map(commission -> this.convertToCommissionListResponse(commission));

        PaginationResponse<List<CommissionListResponse>> response = PaginationResponse.of(dtoPage);

        return response;
    }

    public CommissionListResponse convertToCommissionListResponse(NotaryCommission commission) {
        CommissionListResponse response = new CommissionListResponse();
        response.setId(commission.getId());
        response.setCommissionNumber(commission.getCommissionNumber());
        response.setCommissionState(commission.getState().getStateName());
        response.setIssueDate(commission.getIssueDate());
        response.setExpirationDate(commission.getExpirationDate());
        response.setStatus(commission.getStatus());
        return response;
    }

    public CommissionDetailResponse getCommissionById(UUID notaryId, UUID commissionId) {

        NotaryCommission commission = notaryCommissionRepository.findById(commissionId)
                .orElseThrow(() -> NotFoundException.commission());
        if (!commission.getNotary().getId().equals(notaryId)) {
            throw ForbiddenException.accessDenied();
        }
        return convertToDetailResponse(commission);
    }

    public CommissionDetailResponse convertToDetailResponse(NotaryCommission commission) {
        CommissionDetailResponse response = new CommissionDetailResponse();
        response.setId(commission.getId());
        response.setCommissionNumber(commission.getCommissionNumber());
        response.setCommissionState(commission.getState().getStateName());
        response.setIssueDate(commission.getIssueDate());
        response.setExpirationDate(commission.getExpirationDate());
        response.setExpectedRenewalDate(commission.getExpectedRenewalDate());
        response.setStatus(commission.getStatus());
        response.setIsRenewalApplied(commission.getIsRenewalApplied());
        response.setCreatedAt(commission.getCreatedAt());
        response.setUpdatedAt(commission.getUpdatedAt());
        return response;
    }
}
