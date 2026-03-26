package com.mockproject.notary_common.entity.notary;

import com.mockproject.notary_common.constant.DigitalStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

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
 * 26-03-2026   VanTu               Edit
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ron_technologies")
public class RonTechnology {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Builder.Default
    @Column(name = "ron_camera_ready")
    private boolean ronCameraReady = false;

    @Builder.Default
    @Column(name = "ron_internet_ready")
    private boolean ronInternetReady = false;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "digital_status", length = 16)
    private DigitalStatus digitalStatus = DigitalStatus.INACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "capability_id")
    private NotaryCapability capability;
}