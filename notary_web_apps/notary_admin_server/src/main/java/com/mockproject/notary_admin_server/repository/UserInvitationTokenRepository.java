package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.entity.UserInvitationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserInvitationTokenRepository extends JpaRepository<UserInvitationToken, UUID> {
    Optional<UserInvitationToken> findByToken(String token);
}