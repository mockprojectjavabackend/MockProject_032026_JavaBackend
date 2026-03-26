package com.mockproject.notary_common.entity.notary;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * NotaryAuditLogEntity
 *
 * @version 1.0
 * @date 25-03-2026
 * <p>
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 25-03-2026      VanHai      Create
 * 26-03-2026      VanTu       Edit
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notary_capabilities")
public class NotaryCapability {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Builder.Default
    private boolean mobile = false;

    @Builder.Default
    private boolean ron = false;

    @Builder.Default
    @Column(name = "loan_signing")
    private boolean loanSigning = false;

    @Builder.Default
    @Column(name = "apostille_related_support")
    private boolean apostilleRelatedSupport = false;

    @Column(name = "max_distance")
    private float maxDistance;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "notary_id")
    private Notary notary;

    @OneToOne(mappedBy = "capability")
    private RonTechnology technology;
}