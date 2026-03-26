package com.mockproject.notary_common.entity.notary;

import jakarta.persistence.*;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * NotaryBonds
 *
 * @version 1.0
 * @date 25-03-2026
 * <p>
 * Modification Logs:
 * DATE            AUTHOR       DESCRIPTION
 * -----------------------------------------------
 * 25-03-2026      HuyenThuong  Create
 * 26-03-2026      VanTu        Edit
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notary_bonds")
public class NotaryBonds {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "provider_name", nullable = false, length = 64)
    private String providerName;

    @Column(name = "bond_amount", nullable = false, precision = 13, scale = 2)
    private BigDecimal bondAmount;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "file_url")
    private String fileUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "notary_id", nullable = false)
    private Notary notary;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (effectiveDate != null && expirationDate != null) {
            if (!expirationDate.isAfter(effectiveDate)) {
                throw new IllegalArgumentException("expirationDate must be after effectiveDate");
            }
        }
    }
}