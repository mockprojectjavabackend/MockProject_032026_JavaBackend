package com.mockproject.notary_admin_server.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mockproject.notary_common.entity.notary.NotaryServiceArea;

/**
 * NotaryServiceAreaRepository
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 28-03-2026      DangQuoc      create
 */
@Repository
public interface NotaryServiceAreaRepository extends JpaRepository<NotaryServiceArea, UUID> {

    @Query("""
    SELECT sa.countyName
    FROM NotaryServiceArea sa
    WHERE sa.notary.id = :id
    """)
    List<String> getServiceAreas(UUID id);
}
