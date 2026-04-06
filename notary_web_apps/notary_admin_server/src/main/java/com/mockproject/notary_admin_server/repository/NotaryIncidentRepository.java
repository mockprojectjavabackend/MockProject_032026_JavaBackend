package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.entity.notary.NotaryIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotaryIncidentRepository
        extends JpaRepository<NotaryIncident, UUID> {

    List<NotaryIncident> findByNotaryIdOrderByCreatedAtDesc(UUID notaryId);
}