package com.mockproject.notary_admin_server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.mockproject.notary_common.entity.notary.NotaryServiceArea;

/**
 * NotaryServiceAreaRepository
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 30-03-2026      PhamTam      create
 */

@Repository
public interface NotaryServiceAreaRepository extends JpaRepository<NotaryServiceArea, UUID>, JpaSpecificationExecutor<NotaryServiceArea> {

  List<NotaryServiceArea> findByNotaryId(UUID notaryId);

  @Modifying
  @Query("UPDATE NotaryServiceArea n SET n.deleteAt = :now WHERE n.notary.id = :notaryId AND n.state.id NOT IN :stateIds AND n.deleteAt IS NULL")
  void softDeleteExcluding(@Param("notaryId") UUID notaryId,
                             @Param("stateIds") List<UUID> stateIds,
                             @Param("now") LocalDateTime now);

  @Modifying
  @Query("UPDATE NotaryServiceArea n SET n.deleteAt = NULL WHERE n.notary.id = :notaryId AND n.state.id IN :stateIds AND n.deleteAt IS NOT NULL")
  void restoreByStateIds(@Param("notaryId") UUID notaryId,
                         @Param("stateIds") List<UUID> stateIds);

  @Query("SELECT n.state.id FROM NotaryServiceArea n WHERE n.notary.id = :notaryId")
  List<UUID> findStateIdsByNotaryId(@Param("notaryId") UUID notaryId);
}
