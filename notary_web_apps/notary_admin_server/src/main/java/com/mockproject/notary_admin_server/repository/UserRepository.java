package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * UserRepository
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 29-03-2026      TranMinh    create
 */
public interface UserRepository extends JpaRepository<User, UUID> {
}
