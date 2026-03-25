package com.nhom03.mockproject.entity;

import com.nhom03.mockproject.constant.NotaryHistoryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notary_status_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaryStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "legacy_id", columnDefinition = "BINARY(16)")
    private UUID legacyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notary_id", nullable = false, foreignKey = @ForeignKey(name = "fk_status_history_notary"))
    private Notary notary;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private NotaryHistoryStatus status;

    @Column(name = "reason", length = 500)
    private String reason;

    @Column(name = "effective_at", nullable = false)
    private LocalDateTime effectiveAt;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "created_by_user_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID createdByUserId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
