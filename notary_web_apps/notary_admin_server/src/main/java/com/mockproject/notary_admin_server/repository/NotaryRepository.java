package com.mockproject.notary_admin_server.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mockproject.notary_common.entity.notary.Notary;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * NotaryRepository
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      PhamTam      create
 * * 27-03-2026      TranMinh    create
 *  * 29-03-2026      TranMinh    modify
 */

@Repository
public interface NotaryRepository extends JpaRepository<Notary, UUID>, JpaSpecificationExecutor<Notary> {
    boolean existsBySsn(String ssn);

    boolean existsByUser_Id(UUID userId);

    boolean existsByIdAndUserId(UUID id, UUID userId);

    @EntityGraph(attributePaths = {"user"})
    Optional<Notary> findById(UUID id);
}

