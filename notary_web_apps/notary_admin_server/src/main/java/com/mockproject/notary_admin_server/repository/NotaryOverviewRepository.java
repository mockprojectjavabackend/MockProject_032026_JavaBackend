package com.mockproject.notary_admin_server.repository;

import java.util.List;
import java.util.UUID;

import com.mockproject.notary_admin_server.dto.response.NotaryDetailResponse;
import com.mockproject.notary_common.entity.notary.Notary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * NotaryOverviewRepository
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 26-03-2026      DangQuoc      create
 */
public interface NotaryOverviewRepository extends JpaRepository<Notary, UUID> {

    /**
     * get overview information of a notary
     *
     * Purpose:
     * - Retrieve full related data of a Notary including:
     *   + commissions
     *   + bonds
     *   + insurances
     *   + documents
     *   + service areas
     *   + capability
     *
     * @param notaryId the unique identifier of the notary
     * @return List<Object[]> containing:
     *         [0] Notary (n)
     *         [1] Commission (c)
     *         [2] Bond (b)
     *         [3] Insurance (i)
     *         [4] Document (d)
     *         [5] NotaryServiceArea (sa)
     *         [6] Capability (cap)
     */
    @Query("""
    SELECT n, c, b, i, d, sa, cap
    FROM Notary n
    LEFT JOIN n.commissions c
    LEFT JOIN n.bonds b
    LEFT JOIN n.insurances i
    LEFT JOIN n.documents d
    LEFT JOIN NotaryServiceArea sa ON sa.notary.id = n.id
    LEFT JOIN n.capability cap
    WHERE n.id = :notaryId
    """)
    List<Object[]> getNatoryOverview(@Param("notaryId") UUID notaryId);

    @Query("""
    SELECT new com.mockproject.notary_admin_server.dto.response.NotaryDetailResponse(n.photoUrl,n.fullName,c.commissionNumber,n.email,n.phone,n.address,n.status)
    FROM Notary n
    LEFT JOIN n.commissions c
    WHERE n.id = :notaryId
    """)
    NotaryDetailResponse getNatoryDetails(@Param("notaryId") UUID notaryId);
}
