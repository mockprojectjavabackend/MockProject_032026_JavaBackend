package com.nhom03.mockproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notary_capabilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaryCapability {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    // Quan hệ 1-1 với bảng notaries
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notary_id", referencedColumnName = "id", nullable = false, unique = true)
    private Notary notary;

    @Column(name = "mobile", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean mobile = false;

    @Column(name = "ron", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean ron = false;

    @Column(name = "loan_signing", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean loanSigning = false;

    @Column(name = "apostille_related_support", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean apostilleRelatedSupport = false;

    @Column(name = "max_distance")
    private Integer maxDistance;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
