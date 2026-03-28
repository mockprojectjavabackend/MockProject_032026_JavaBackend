package com.mockproject.notary_common.entity.notary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mockproject.notary_common.constant.AuthorityType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * AuthorityScope
 * <p>
 * Version 1.0
 * <p>
 * Date: 25-03-2026
 * <p>
 * Description:
 * This entity represents the "authority_scopes" table in database.
 * It defines the authority scope assigned to a specific notary commission.
 * <p>
 * Business Rules:
 * - Each commission can have multiple authority scopes.
 * - Combination of (commission_id, authority_type) must be unique.
 * <p>
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------
 * 25-03-2026 DangQuoc Create
 * 26-03-2026 VanTu Edit
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "authority_scopes")
@SQLRestriction("is_deleted = false")
public class AuthorityScope {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority_type", length = 32)
    private AuthorityType authorityType;

    @ManyToOne
    @JoinColumn(name = "commission_id")
    @JsonIgnore
    private NotaryCommission commission;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
