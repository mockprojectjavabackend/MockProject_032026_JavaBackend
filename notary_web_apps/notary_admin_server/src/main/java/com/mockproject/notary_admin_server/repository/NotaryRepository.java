package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.entity.notary.Notary;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * NotaryRepository
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      TranMinh    create
 * 29-03-2026      TranMinh    modify
 */
@Repository
public interface NotaryRepository extends JpaRepository<Notary, UUID>, JpaSpecificationExecutor<Notary> {

    boolean existsByEmail(String email);

    boolean existsByUser_Id(UUID userId);

    boolean existsByEmailAndIdNot(String email, UUID id);

    @EntityGraph(attributePaths = {"user"})
    Optional<Notary> findById(UUID id);
}

