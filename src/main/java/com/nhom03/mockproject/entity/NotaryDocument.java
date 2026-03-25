package com.nhom03.mockproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

import java.util.UUID;

import com.nhom03.mockproject.constant.DocCategory;
import com.nhom03.mockproject.constant.VerifiedStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

/**
 * NotaryDocument Entity
 */
@Entity
@Table(
    name = "notary_documents",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_notary_documents_cat_version", columnNames = {"notary_id", "doc_category", "version"}),
        @UniqueConstraint(name = "uq_notary_documents_one_current", columnNames = {"notary_id", "current_doc_category"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaryDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notary_id", nullable = false)
    private Notary notary;

    @Enumerated(EnumType.STRING)
    @Column(name = "doc_category", nullable = false, length = 60)
    private DocCategory docCategory;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "verified_status", nullable = false, length = 20)
    @Builder.Default
    private VerifiedStatus verifiedStatus = VerifiedStatus.PENDING;

    @Column(name = "version", nullable = false)
    @Builder.Default
    private Integer version = 1;

    @Column(name = "is_current_version", nullable = false)
    @Builder.Default
    private Boolean isCurrentVersion = true;

    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Computed column in DB
    @Column(name = "current_doc_category", insertable = false, updatable = false, length = 60)
    private String currentDocCategory;


}
