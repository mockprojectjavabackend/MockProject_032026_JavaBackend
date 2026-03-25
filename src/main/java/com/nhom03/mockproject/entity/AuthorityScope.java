package com.nhom03.mockproject.entity;

import java.util.UUID;

import com.nhom03.mockproject.constant.AuthorityType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * AuthorityScope
 *
 * Version 1.0
 *
 * Date: 25-03-2026
 *
 * Description:
 * This entity represents the "authority_scopes" table in database.
 * It defines the authority scope assigned to a specific notary commission.
 *
 * Business Rules:
 * - Each commission can have multiple authority scopes.
 * - Combination of (commission_id, authority_type) must be unique.
 *
 * Fields:
 * - id: Primary key (UUID)
 * - commission: Reference to NotaryCommission
 * - authorityType: Type of authority assigned
 *
 * Copyright (c) 2026
 *
 * Modification Logs:
 * DATE         AUTHOR              DESCRIPTION
 * ----------------------------------------------------------
 * 25-03-2026   DangQuoc            Create
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "authority_scopes", uniqueConstraints = {
                @UniqueConstraint(name = "uq_authority_comm_type", columnNames = {"commission_id", "authority_type"})
        },
        indexes = {
                @Index(name = "idx_authority_commission_id", columnList = "commission_id")
})
public class AuthorityScope {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_id", nullable = false)
    private NotaryCommission commission;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority_type", nullable = false, length = 50)
    private AuthorityType authorityType;

}
