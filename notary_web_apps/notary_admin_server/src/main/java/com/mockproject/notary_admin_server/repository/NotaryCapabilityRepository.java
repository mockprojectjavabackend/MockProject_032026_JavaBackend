package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.entity.notary.NotaryCapability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotaryCapabilityRepository extends JpaRepository<NotaryCapability, UUID> {
    NotaryCapability findByNotary_Id(UUID id);
}
