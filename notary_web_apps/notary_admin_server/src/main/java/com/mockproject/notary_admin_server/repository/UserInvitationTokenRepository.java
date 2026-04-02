package com.mockproject.notary_admin_server.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mockproject.notary_common.entity.UserInvitationToken;

public interface UserInvitationTokenRepository extends JpaRepository<UserInvitationToken, UUID> {
    Optional<UserInvitationToken> findByToken(String token);
}
