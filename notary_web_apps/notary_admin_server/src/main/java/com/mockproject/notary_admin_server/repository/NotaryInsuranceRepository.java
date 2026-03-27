package com.mockproject.notary_admin_server.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.mockproject.notary_common.entity.notary.NotaryInsurance;

@Repository
public interface NotaryInsuranceRepository
        extends JpaRepository<NotaryInsurance, UUID>, JpaSpecificationExecutor<NotaryInsurance> {

    Optional<NotaryInsurance> findFirstByNotary_IdOrderByCreatedAtDesc(UUID notaryId);

    List<NotaryInsurance> findAllByNotary_IdOrderByCreatedAtDesc(UUID notaryId);

}