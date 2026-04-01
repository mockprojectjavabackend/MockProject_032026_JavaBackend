package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.constant.AuditLogAction;
import com.mockproject.notary_common.entity.notary.NotaryAuditLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotaryAuditLogRepository
                extends JpaRepository<NotaryAuditLog, UUID> {

        @Query("SELECT n FROM NotaryAuditLog n " +
                        "WHERE n.notary.id = :notaryId " +
                        "AND n.createdAt >= :fromTime " +
                        "ORDER BY n.createdAt DESC")
        List<NotaryAuditLog> findByNotaryIdAndTimeRange(
                        @Param("notaryId") UUID notaryId,
                        @Param("fromTime") LocalDateTime fromTime,
                        Pageable pageable);

        @Query("SELECT n FROM NotaryAuditLog n " +
                        "WHERE n.notary.id = :notaryId " +
                        "ORDER BY n.createdAt DESC")
        List<NotaryAuditLog> findRecentActivities(
                        @Param("notaryId") UUID notaryId,
                        Pageable pageable);

        @Query("SELECT COUNT(n) FROM NotaryAuditLog n " +
                        "WHERE n.notary.id = :notaryId " +
                        "AND n.createdAt >= :fromTime")
        long countByNotaryIdAndTimeRange(
                        @Param("notaryId") UUID notaryId,
                        @Param("fromTime") LocalDateTime fromTime);

        @Query("SELECT n FROM NotaryAuditLog n " +
                        "WHERE n.notary.id = :notaryId " +
                        "AND n.createdAt >= :fromTime " +
                        "AND (:userId IS NULL OR n.changeByUserId = :userId) " +
                        "AND (:action IS NULL OR n.action = :action) " +
                        "ORDER BY n.createdAt DESC")
        List<NotaryAuditLog> findByNotaryIdWithFilters(
                        @Param("notaryId") UUID notaryId,
                        @Param("fromTime") LocalDateTime fromTime,
                        @Param("userId") UUID userId,
                        @Param("action") AuditLogAction action,
                        Pageable pageable);

        @Query("SELECT COUNT(n) FROM NotaryAuditLog n " +
                        "WHERE n.notary.id = :notaryId " +
                        "AND n.createdAt >= :fromTime " +
                        "AND (:userId IS NULL OR n.changeByUserId = :userId) " +
                        "AND (:action IS NULL OR n.action = :action)")
        long countByNotaryIdWithFilters(
                        @Param("notaryId") UUID notaryId,
                        @Param("fromTime") LocalDateTime fromTime,
                        @Param("userId") UUID userId,
                        @Param("action") AuditLogAction action);

        @Query("SELECT n FROM NotaryAuditLog n WHERE n.id = :auditId AND n.notary.id = :notaryId")
        Optional<NotaryAuditLog> findByIdAndNotaryId(
                        @Param("auditId") UUID auditId,
                        @Param("notaryId") UUID notaryId);
}