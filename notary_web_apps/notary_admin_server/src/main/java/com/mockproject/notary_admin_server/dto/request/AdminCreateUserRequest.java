package com.mockproject.notary_admin_server.dto.request;

import jakarta.validation.constraints.*;

import com.mockproject.notary_common.constant.PredefinedRole;

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
public class AdminCreateUserRequest {
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    String email;

    @NotNull(message = "Role không được để trống")
    PredefinedRole role;
}
