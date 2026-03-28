package com.mockproject.notary_admin_server.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.mockproject.notary_admin_server.dto.PaginationResponse;
import com.mockproject.notary_admin_server.dto.request.CommissionQuery;
import com.mockproject.notary_admin_server.dto.response.CommissionDetailResponse;
import com.mockproject.notary_admin_server.dto.response.CommissionListResponse;
import com.mockproject.notary_common.entity.notary.NotaryCommission;

public interface NotaryCommissionService {
    PaginationResponse<List<CommissionListResponse>> fetchAllNotaryCommissions(UUID notaryId, CommissionQuery query);

    CommissionDetailResponse getCommissionById(UUID notaryId, UUID commissionId);
}
