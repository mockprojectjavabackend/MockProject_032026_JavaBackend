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
import com.mockproject.notary_admin_server.dto.request.CreateNotaryCommissionRequest;
import com.mockproject.notary_admin_server.dto.request.UpdateNotaryCommissionRequest;
import com.mockproject.notary_admin_server.dto.response.CommissionDetailResponse;
import com.mockproject.notary_admin_server.dto.response.CommissionListResponse;
import com.mockproject.notary_admin_server.exception.ForbiddenException;
import com.mockproject.notary_admin_server.exception.NotFoundException;
import com.mockproject.notary_admin_server.repository.NotaryCommissionRepository;
import com.mockproject.notary_admin_server.repository.NotaryRepository;
import com.mockproject.notary_admin_server.repository.StateRepository;
import com.mockproject.notary_admin_server.service.NotaryCommissionService;
import com.mockproject.notary_common.constant.CommissionStatus;
import com.mockproject.notary_common.entity.State;
import com.mockproject.notary_common.entity.notary.Notary;
import com.mockproject.notary_common.entity.notary.NotaryCommission;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * NotaryCommissionServiceImpl
 *
 * @version 1.0
 * @date 29-03-2026
 *       <p>
 *       Modification Logs:
 *       DATE AUTHOR DESCRIPTION
 *       -----------------------------------------------
 *       29-03-2026 HuyenThuong handle logic crud notary commission
 */

@Service
public class NotaryCommissionServiceImpl implements NotaryCommissionService {
    private final NotaryCommissionRepository notaryCommissionRepository;
    private final NotaryRepository notaryRepository;
    private final StateRepository stateRepository;

    public NotaryCommissionServiceImpl(NotaryCommissionRepository notaryCommissionRepository,
            NotaryRepository notaryRepository, StateRepository stateRepository) {
        this.notaryCommissionRepository = notaryCommissionRepository;
        this.notaryRepository = notaryRepository;
        this.stateRepository = stateRepository;

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
        response.setNotaryId(commission.getNotary().getId());
        response.setIssueDate(commission.getIssueDate());
        response.setExpirationDate(commission.getExpirationDate());
        response.setExpectedRenewalDate(commission.getExpectedRenewalDate());
        response.setStatus(commission.getStatus());
        response.setIsRenewalApplied(commission.getIsRenewalApplied());
        response.setFileUrl(commission.getFileUrl());
        response.setCreatedAt(commission.getCreatedAt());
        response.setUpdatedAt(commission.getUpdatedAt());
        return response;
    }

    public CommissionDetailResponse createCommission(UUID notaryId, CreateNotaryCommissionRequest req) {

        Notary notary = notaryRepository.findById(notaryId).orElseThrow(() -> NotFoundException.notary());

        State state = stateRepository.findById(req.getCommissionStateId()).orElseThrow(() -> NotFoundException.state());
        NotaryCommission commission = new NotaryCommission();
        commission.setNotary(notary);
        commission.setState(state);
        commission.setCommissionNumber(req.getCommissionNumber());
        commission.setIssueDate(req.getIssueDate());
        commission.setExpirationDate(req.getExpirationDate());
        commission.setExpectedRenewalDate(req.getExpectedRenewalDate());
        commission.setFileUrl(req.getFileUrl());

        notaryCommissionRepository.save(commission);

        return convertToDetailResponse(commission);
    }

    public CommissionDetailResponse updateCommission(UUID notaryId, UUID commissionId,
            UpdateNotaryCommissionRequest req) {

        NotaryCommission commission = notaryCommissionRepository.findById(commissionId)
                .orElseThrow(() -> NotFoundException.commission());

        if (!commission.getNotary().getId().equals(notaryId)) {
            throw ForbiddenException.accessDenied();
        }

        commission.setCommissionNumber(req.getCommissionNumber());
        commission.setIssueDate(req.getIssueDate());
        commission.setExpirationDate(req.getExpirationDate());
        commission.setExpectedRenewalDate(req.getExpectedRenewalDate());

        if (req.getCommissionStateId() != null) {
            State state = stateRepository.findById(req.getCommissionStateId())
                    .orElseThrow(() -> NotFoundException.state());

            commission.setState(state);
        }
        commission.setStatus(req.getStatus());
        commission.setIsRenewalApplied(req.getIsRenewalApplied());

        if (req.getFileUrl() != null) {
            commission.setFileUrl(req.getFileUrl());
        }
        notaryCommissionRepository.save(commission);

        return convertToDetailResponse(commission);
    }

    public void deleteCommission(UUID notaryId, UUID commissionId) {

        NotaryCommission commission = notaryCommissionRepository.findById(commissionId)
                .orElseThrow(() -> NotFoundException.commission());

        if (!commission.getNotary().getId().equals(notaryId)) {
            throw ForbiddenException.accessDenied();
        }

        commission.setIsDeleted(true);

        notaryCommissionRepository.save(commission);
    }
}
