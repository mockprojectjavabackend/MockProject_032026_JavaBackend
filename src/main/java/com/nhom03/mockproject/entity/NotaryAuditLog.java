package com.nhom03.mockproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * NotaryAuditLogEntity
 *
 * @version 1.0
 *
 * @date 25-03-2026
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 25-03-2026      phamtam        Create notary audit log entity
 */

@Table (
     name = "notary_audit_logs"
)
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaryAuditLog {

    // Enums
    public enum Action {
        INSERT, UPDATE, DELETE
    }

    // Fields
    @Id
    @UuidGenerator
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "table_name", length = 128, nullable = false)
    private String tableName;

    @Column(name = "record_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID recordId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", length = 20, nullable = false)
    private Action action;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "old_value", columnDefinition = "JSON")
    private Map<String, Object> oldValue;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "new_value", columnDefinition = "JSON")
    private Map<String, Object> newValue;

    @Column(name = "change_by_user_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID changeByUserId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notary_id", nullable = false)
    private Notary notary;

    // Lifecycle hooks
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
