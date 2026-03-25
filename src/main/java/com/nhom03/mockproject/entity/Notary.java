package com.nhom03.mockproject.entity;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * NotaryEntity
 *
 * @version 1.0
 *
 * @date 25-03-2026
 *
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 25-03-2026      phamtam        Create notary entity
 */
@Table(
        name = "notaries",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_notaries_user_id", columnNames = "user_id"),
                @UniqueConstraint(name = "uq_notaries_ssn",     columnNames = "ssn")
        }
)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Notary {

    // Enums
    public enum Status {
        ACTIVE, INACTIVE, BLOCKED
    }

    public enum EmploymentType {
        FULL_TIME, PART_TIME, INDEPENDENT_CONTRACT
    }

    // Fields
    @Id
    @UuidGenerator
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID userId;

    @Column(name = "ssn", length = 20, nullable = false)
    private String ssn;

    @Column(name = "full_name", length = 255, nullable = false)
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "email", length = 255)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", length = 40)
    private EmploymentType employmentType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Column(name = "residential_address", length = 500)
    private String residentialAddress;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    // Lifecycle hooks
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
