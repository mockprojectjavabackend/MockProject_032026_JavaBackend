package com.nhom03.mockproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.nhom03.mockproject.constant.CommissionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notary_commissions", uniqueConstraints = {
        @UniqueConstraint(name = "uq_commissions_state_number", columnNames = { "commission_state",
                "commission_number" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaryCommission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    // Quan hệ N-1: Nhiều Commission thuộc về 1 Notary
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notary_id", nullable = false)
    private Notary notary;

    @Column(name = "commission_state", nullable = false, length = 2)
    private String commissionState;

    @Column(name = "commission_number", nullable = false, length = 100)
    private String commissionNumber;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private CommissionStatus status;

    @Column(name = "is_renewal_applied", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isRenewalApplied = false;

    @Column(name = "expected_renewal_date")
    private LocalDate expectedRenewalDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
