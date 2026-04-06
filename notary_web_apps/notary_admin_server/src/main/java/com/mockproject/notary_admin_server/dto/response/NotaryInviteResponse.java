package com.mockproject.notary_admin_server.dto.response;

import java.util.UUID;

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
public class NotaryInviteResponse {
    UUID userId;
    UUID notaryId;
    String email;
    UserStatus userStatus;
    boolean invitationSent;
}
