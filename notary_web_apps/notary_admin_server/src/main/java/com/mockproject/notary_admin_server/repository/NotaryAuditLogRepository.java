package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.entity.notary.NotaryAuditLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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
}