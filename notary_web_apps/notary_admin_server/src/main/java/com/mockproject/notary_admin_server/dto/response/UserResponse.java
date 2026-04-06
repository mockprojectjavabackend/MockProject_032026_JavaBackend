package com.mockproject.notary_admin_server.dto.response;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.mockproject.notary_common.constant.PredefinedRole;
import com.mockproject.notary_common.constant.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserResponse {
    UUID id;
    String email;
    UserStatus status;
    Set<PredefinedRole> roles;
    LocalDateTime createdAt;
}
