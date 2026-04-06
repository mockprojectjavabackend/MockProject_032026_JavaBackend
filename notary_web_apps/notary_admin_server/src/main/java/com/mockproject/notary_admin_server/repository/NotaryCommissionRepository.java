package com.mockproject.notary_admin_server.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.mockproject.notary_common.entity.notary.NotaryCommission;

@Repository
public interface NotaryCommissionRepository
        extends JpaRepository<NotaryCommission, UUID>, JpaSpecificationExecutor<NotaryCommission> {
}
