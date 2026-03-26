package com.mockproject.notary_common.entity.notary;

import com.mockproject.notary_common.constant.CommissionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * NotaryCommission
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 25-03-2026      VanHai      create
 * 26-03-2026      VanTu       edit
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notary_commissions")
public class NotaryCommission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "commission_state", nullable = false, length = 2)
    private String commissionState;

    @Column(name = "commission_number", nullable = false, length = 64)
    private String commissionNumber;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private CommissionStatus status = CommissionStatus.INVALID;

    @Builder.Default
    @Column(name = "is_renewal_applied")
    private boolean isRenewalApplied = false;

    @Column(name = "expected_renewal_date")
    private LocalDate expectedRenewalDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "notary_id")
    private Notary notary;

    @Builder.Default
    @OneToMany(mappedBy = "commission")
    private Set<AuthorityScope> authorityScopes = new HashSet<>();
}