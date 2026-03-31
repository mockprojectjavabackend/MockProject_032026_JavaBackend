package com.mockproject.notary_admin_server.dto.response;

import com.mockproject.notary_common.constant.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserResponse {
    UUID id;
    String email;
    String phoneNumber;
    String fullName;
    LocalDate dob;
    String address;
    UserStatus status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
