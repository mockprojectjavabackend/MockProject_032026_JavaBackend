package com.nhom03.mockproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notary_insurances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaryInsurance {

    @Id
    @Column(columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notary_id", nullable = false)
    private Notary notary;

    @Size(max = 100)
    @Column(name = "policy_number", length = 100, nullable = false, unique = true)
    private String policyNumber;

    @Size(max = 255)
    @Column(name = "provider_name", length = 255, nullable = false)
    private String providerName;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "coverage_amount", precision = 13, scale = 2, nullable = false)
    private BigDecimal coverageAmount;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Size(max = 500)
    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
