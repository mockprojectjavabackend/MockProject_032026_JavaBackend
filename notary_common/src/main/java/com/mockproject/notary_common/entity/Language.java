package com.mockproject.notary_common.entity;

import com.mockproject.notary_common.entity.notary.Notary;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Set;
import java.util.UUID;

/**
 * Language
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 25-03-2026      DangQuoc    create
 * 26-03-2026      VanTu       edit
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "languages")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "lang_code", nullable = false, length = 8, unique = true)
    private String langCode;

    @Column(name = "lang_name", nullable = false, length = 64)
    private String langName;

    @ManyToMany(mappedBy = "languages")
    private Set<Notary> notaries;
}