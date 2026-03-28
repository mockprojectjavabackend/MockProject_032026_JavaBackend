package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.entity.notary.Notary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * NotaryController
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      PhamTam      create
 */

@Repository
public interface NotaryRepository extends JpaRepository<Notary, UUID>, JpaSpecificationExecutor<Notary> {

}
