package com.nhom03.mockproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Profile Entity
 *
 * Represents Profile table in database.
 *
 * Version 1.0
 *
 * Date: 17-03-2026
 *
 * Modification Logs:
 * DATE         AUTHOR      DESCRIPTION
 * ----------------------------------------------------------
 * 17-03-2026   Quoc        Create
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
}
