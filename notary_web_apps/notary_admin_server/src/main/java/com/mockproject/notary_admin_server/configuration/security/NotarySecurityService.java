package com.mockproject.notary_admin_server.configuration.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

import com.mockproject.notary_admin_server.repository.NotaryRepository;
import org.springframework.stereotype.Service;

@Service("notarySecurityService")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotarySecurityService {
    NotaryRepository notaryRepository;
    public boolean isOwner(UUID notaryId, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        List<String> roles = jwt.getClaimAsStringList("scope");
        if (roles != null && roles.contains("ROLE_ADMIN")) return true;
        UUID userId = UUID.fromString(jwt.getClaimAsString("user_id"));
        return notaryRepository.existsByIdAndUserId(notaryId, userId);
    }
}
