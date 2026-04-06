package com.mockproject.notary_common.entity.notary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mockproject.notary_common.constant.EmploymentType;
import com.mockproject.notary_common.constant.UserStatus;
import com.mockproject.notary_common.entity.Language;
import com.mockproject.notary_common.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * NotaryEntity
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 25-03-2026      PhamTam      create
 * 26-03-2026      VanTu        edit
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notaries")
public class Notary {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "ssn", length = 32, nullable = false)
    private String ssn;

    @Column(name = "full_name", length = 64, nullable = false)
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(length = 16)
    private String phone;

    @Column(name = "email", nullable = false, unique = true, length = 64)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", length = 32)
    private EmploymentType employmentType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "internal_notes")
    private String internalNotes;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private UserStatus status = UserStatus.INACTIVE;

    @Column(length = 128)
    private String address;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "notary")
    private Set<NotaryCommission> commissions = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "notary")
    private Set<NotaryAuditLog> auditLogs = new HashSet<>();

    @OneToOne(mappedBy = "notary")
    private NotaryAvailability availability;

    @Builder.Default
    @OneToMany(mappedBy = "notary")
    private Set<NotaryBonds> bonds = new HashSet<>();

    @OneToOne(mappedBy = "notary")
    private NotaryCapability capability;

    @Builder.Default
    @OneToMany(mappedBy = "notary")
    private Set<NotaryDocument> documents = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "notary")
    private Set<NotaryIncident> incidents = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "notary")
    private Set<NotaryInsurance> insurances = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "notary")
    private Set<NotaryStatusHistory> histories = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "notary", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<NotaryServiceArea> serviceAreas = new HashSet<>();

    // one-way relationship
    @ManyToMany
    @JoinTable(
            name = "notary_languages",
            joinColumns = @JoinColumn(name = "notary_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "language_id", referencedColumnName = "id")
    )
    @Builder.Default
    private Set<Language> languages = new HashSet<>();
}