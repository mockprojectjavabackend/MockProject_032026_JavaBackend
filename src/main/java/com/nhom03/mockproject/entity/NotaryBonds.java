package com.nhom03.mockproject.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notary_bonds")
@Getter
@Setter
public class NotaryBonds {
    @Id
    @UuidGenerator
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notary_id", nullable = false)
    private Notary notary;

    @NotBlank
    @Size(max = 255)
    @Column(name = "provider_name", nullable = false, length = 255)
    private String providerName;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "bond_amount", nullable = false, precision = 13, scale = 2)
    private BigDecimal bondAmount;

    @NotNull
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Size(max = 500)
    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        validate();
    }

    @PreUpdate
    public void preUpdate() {
        validate();
    }

    private void validate() {
        if (effectiveDate != null && expirationDate != null) {
            if (!expirationDate.isAfter(effectiveDate)) {
                throw new IllegalArgumentException("expirationDate must be after effectiveDate");
            }
        }
    }
}
