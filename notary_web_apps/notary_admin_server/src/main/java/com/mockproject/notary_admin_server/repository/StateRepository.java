package com.mockproject.notary_admin_server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

import com.mockproject.notary_common.entity.State;

/**
 * StateRepository
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 30-03-2026      PhamTam      create
 */

@Repository
public interface StateRepository extends JpaRepository<State, UUID>, JpaSpecificationExecutor<State> {

    @Query("""
    SELECT s
    FROM NotaryServiceArea nsa
    JOIN nsa.state s
    WHERE nsa.notary.id = :notaryId
      AND nsa.deleteAt IS NULL
    """)
    List<State> findAllByNotaryId(@Param("notaryId") UUID notaryId);
}
