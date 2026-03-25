package com.nhom03.mockproject.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Language
 *
 * Version 1.0
 *
 * Date: 25-03-2026
 *
 * Description:
 * This entity represents the "languages" table in database.
 * It stores information about supported languages in the system.
 *
 * Fields:
 * - id: Primary key (UUID, stored as BINARY(16))
 * - langCode: Unique language code (e.g., en, vi, jp)
 * - langName: Full language name (e.g., English, Vietnamese)
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
@Table(name = "languages")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "lang_code", nullable = false, length = 10, unique = true)
    private String langCode;

    @Column(name = "lang_name", nullable = false, length = 100)
    private String langName;





}
