package com.mockproject.notary_common.entity.notary;

import com.mockproject.notary_common.constant.DocCategory;
import com.mockproject.notary_common.constant.VerifiedStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * NotaryDocument
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 25-03-2026      QuangAnh    create
 * 26-03-2026      VanTu       edit
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notary_documents")
public class NotaryDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "doc_category", nullable = false, length = 60)
    private DocCategory docCategory;

    @Column(name = "file_name", nullable = false, length = 64)
    private String fileName;

    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "verified_status", length = 16)
    private VerifiedStatus verifiedStatus = VerifiedStatus.PENDING;

    @Builder.Default
    private int version = 1;

    @Builder.Default
    @Column(name = "is_current_version")
    private boolean isCurrentVersion = true;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "notary_id")
    private Notary notary;
}
