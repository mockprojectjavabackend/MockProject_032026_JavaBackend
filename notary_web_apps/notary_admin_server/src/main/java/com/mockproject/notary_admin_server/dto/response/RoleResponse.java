package com.mockproject.notary_admin_server.dto.response;

import com.mockproject.notary_common.constant.PredefinedRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RoleResponse {
    UUID id;
    PredefinedRole roleName;
}
