package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.constant.DocCategory;
import com.mockproject.notary_common.constant.VerifiedStatus;
import com.mockproject.notary_common.entity.notary.NotaryDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * NotaryDocumentRepository
 *
 * @version 1.0
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 26-03-2026      AXL24       create
 */
@Repository
public interface NotaryDocumentRepository extends JpaRepository<NotaryDocument, UUID> {

    /**
     * Find all documents for a notary with optional filters.
     *
     */
    @Query("""
            SELECT d FROM NotaryDocument d
            WHERE d.notary.id = :notaryId
              AND (:status IS NULL OR d.verifiedStatus = :status)
              AND (:docCategory IS NULL OR d.docCategory = :docCategory)
              AND (:fileName IS NULL OR LOWER(d.fileName) LIKE LOWER(CONCAT('%', :fileName, '%')))
              AND (:fromDate IS NULL OR d.uploadDate >= :fromDate)
              AND (:toDate IS NULL OR d.uploadDate <= :toDate)
            """)
    Page<NotaryDocument> findAllByFilters(
            @Param("notaryId") UUID notaryId,
            @Param("status") VerifiedStatus status,
            @Param("docCategory") DocCategory docCategory,
            @Param("fileName") String fileName,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable
    );

    /**
     * Find a document by its ID and notary ID.
     *
     */
    Optional<NotaryDocument> findByIdAndNotaryId(UUID id, UUID notaryId);
}
