package com.mockproject.notary_admin_server.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.mockproject.notary_admin_server.repository.UserRepository;

@Component
public class AuditServiceHelper {

    private final UserRepository userRepository;

    public AuditServiceHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String resolveAdminName(UUID userId) {
        if (userId == null)
            return "Unknown";
        return userRepository.findById(userId)
                .map(user -> user.getFullName())
                .orElse("Unknown");
    }

    public String resolveTimestamp(LocalDateTime createdAt) {
        if (createdAt != null) {
            return createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        return "N/A";
    }
}
