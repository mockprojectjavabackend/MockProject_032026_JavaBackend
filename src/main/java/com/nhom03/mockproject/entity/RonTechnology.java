package com.nhom03.mockproject.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.nhom03.mockproject.constant.DigitalStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * RonTechnology
 *
 * Version 1.0
 *
 * Date: 25-03-2026
 *
 * Description:
 * This entity represents the "ron_technologies" table in database.
 * It stores information about Remote Online Notarization (RON) technology readiness
 * for a specific notary capability.
 *
 * Business Rules:
 * - Each capability can have only one RON technology configuration.
 * - Capability must be unique (uq_ron_capability).
 *
 * Fields:
 * - id: Primary key (UUID)
 * - capability: Reference to NotaryCapability
 * - ronCameraReady: Indicates if camera is ready for RON
 * - ronInternetReady: Indicates if internet is ready for RON
 * - digitalStatus: Status of the digital capability (ACTIVE/INACTIVE)
 * - createdAt: Timestamp when record is created
 *
 * Copyright (c) 2026
 *
 * Modification Logs:
 * DATE         AUTHOR              DESCRIPTION
 * ----------------------------------------------------------
 * 25-03-2026   DangQuoc            Create
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ron_technologies", uniqueConstraints = {
                @UniqueConstraint(name = "uq_ron_capability", columnNames = "capability_id")
})
public class RonTechnology {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capability_id", nullable = false)
    private NotaryCapability capability;

    @Column(name = "ron_camera_ready", nullable = false)
    private boolean ronCameraReady = false;

    @Column(name = "ron_internet_ready", nullable = false)
    private boolean ronInternetReady = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "digital_status", nullable = false, length = 20)
    @Builder.Default
    private DigitalStatus digitalStatus = DigitalStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
