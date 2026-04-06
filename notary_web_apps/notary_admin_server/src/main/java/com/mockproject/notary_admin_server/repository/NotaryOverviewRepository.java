package com.mockproject.notary_admin_server.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mockproject.notary_admin_server.dto.response.NotaryDetailResponse;
import com.mockproject.notary_admin_server.dto.response.NotaryOverviewDTO;
import com.mockproject.notary_common.entity.notary.Notary;

/**
 * NotaryOverviewRepository
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 26-03-2026      DangQuoc      create
 * 28-03-2026      DangQuoc      deleted getNotaryOverview
 * 28-03-2026      DangQuoc      create getOverview
 */
@Repository
public interface NotaryOverviewRepository extends JpaRepository<Notary, UUID> {

    @Query("""
    SELECT new com.mockproject.notary_admin_server.dto.response.NotaryOverviewDTO(
    
        c.status,
        c.expirationDate,
        c.expectedRenewalDate,
        c.isRenewalApplied,
    
        CASE 
            WHEN b.expirationDate < CURRENT_DATE THEN 'Expired'
            ELSE 'Valid'
        END,
        b.bondAmount,
    
        CASE 
            WHEN i.expirationDate < CURRENT_DATE THEN 'Expired'
            ELSE 'Valid'
        END,
        i.expirationDate,
        i.effectiveDate,
    
        d.docCategory,
        d.verifiedStatus,
        d.uploadDate,
    
        u.email,
        n.phone,
        n.address
    )
    FROM Notary n
    JOIN n.user u
    LEFT JOIN n.commissions c
    LEFT JOIN n.bonds b
    LEFT JOIN n.insurances i
    LEFT JOIN n.documents d
    WHERE n.id = :id
    """)
    Optional<NotaryOverviewDTO> getOverview(UUID id);

    @Query("""
    SELECT new com.mockproject.notary_admin_server.dto.response.NotaryDetailResponse(n.photoUrl,n.fullName,c.commissionNumber,u.email,n.phone,n.address,u.status)
    FROM Notary n
    JOIN n.user u
    LEFT JOIN n.commissions c
    WHERE n.id = :notaryId
    """)
    NotaryDetailResponse getNatoryDetails(@Param("notaryId") UUID notaryId);
}
