package com.mockproject.notary_common.entity.notary;

import com.mockproject.notary_common.constant.AuditLogAction;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * NotaryAuditLogEntity
 *
 * @version 1.0
 * @date 25-03-2026
 * <p>
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 25-03-2026      PhamTam     Create
 * 26-03-2026      VanTu       Edit
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notary_audit_logs")
public class NotaryAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "table_name", length = 32, nullable = false)
    private String tableName;

    @Column(name = "record_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID recordId;

    @Enumerated(EnumType.STRING)
    @Column(length = 16, nullable = false)
    private AuditLogAction action;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "old_value", columnDefinition = "JSON", nullable = false)
    private Map<String, Object> oldValue;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "new_value", columnDefinition = "JSON", nullable = false)
    private Map<String, Object> newValue;

    @Column(name = "change_by_user_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID changeByUserId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "notary_id")
    private Notary notary;
}