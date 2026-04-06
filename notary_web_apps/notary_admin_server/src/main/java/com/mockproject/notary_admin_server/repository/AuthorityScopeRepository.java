package com.mockproject.notary_admin_server.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mockproject.notary_common.constant.AuthorityType;
import com.mockproject.notary_common.entity.notary.AuthorityScope;

@Repository
public interface AuthorityScopeRepository extends JpaRepository<AuthorityScope, UUID> {

    boolean existsByCommissionIdAndAuthorityType(UUID commissionId, AuthorityType type);

    List<AuthorityScope> findByCommissionId(UUID commissionId);
}
