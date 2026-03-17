package com.nhom03.mockproject.repository;

import com.nhom03.mockproject.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProfileRepository
 *
 * Repository interface for Profile entity.
 * Provides CRUD operations using JPA.
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
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
