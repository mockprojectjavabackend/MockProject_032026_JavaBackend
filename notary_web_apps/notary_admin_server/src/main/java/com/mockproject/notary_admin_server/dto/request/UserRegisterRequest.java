package com.mockproject.notary_admin_server.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserRegisterRequest {
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu không được nhỏ hơn 6 ký tự")
    String password;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
    String phoneNumber;

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 64, message = "Họ và tên không được vượt quá 64 ký tự")
    String fullName;

    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    LocalDate dob;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 128, message = "Địa chỉ không được vượt quá 128 ký tự")
    String address;
}
