package com.mockproject.notary_admin_server.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mockproject.notary_admin_server.repository.AuthorityScopeRepository;
import com.mockproject.notary_admin_server.repository.NotaryCommissionRepository;
import com.mockproject.notary_admin_server.service.AuthorityScopeService;
import com.mockproject.notary_common.constant.AuthorityType;
import com.mockproject.notary_common.entity.notary.AuthorityScope;
import com.mockproject.notary_common.entity.notary.NotaryCommission;

@Service
public class AuthorityScopeServiceImpl implements AuthorityScopeService {

    private final AuthorityScopeRepository authorityScopeRepository;
    private final NotaryCommissionRepository commissionRepository;

    public AuthorityScopeServiceImpl(AuthorityScopeRepository authorityScopeRepository,
            NotaryCommissionRepository commissionRepository) {
        this.authorityScopeRepository = authorityScopeRepository;
        this.commissionRepository = commissionRepository;
    }

    public AuthorityScope create(UUID commissionId, AuthorityType authorityType) {

        if (authorityScopeRepository
                .existsByCommissionIdAndAuthorityType(commissionId, authorityType)) {
            throw new RuntimeException("Authority already exists for this commission");
        }

        NotaryCommission commission = commissionRepository.findById(commissionId)
                .orElseThrow(() -> new RuntimeException("Commission not found"));

        AuthorityScope scope = AuthorityScope.builder()
                .authorityType(authorityType)
                .commission(commission)
                .build();

        return authorityScopeRepository.save(scope);
    }

    public List<AuthorityScope> getByCommission(UUID commissionId) {
        return authorityScopeRepository.findByCommissionId(commissionId);
    }

    public void delete(UUID id) {

        AuthorityScope scope = authorityScopeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AuthorityScope not found"));

        authorityScopeRepository.delete(scope);
    }
}