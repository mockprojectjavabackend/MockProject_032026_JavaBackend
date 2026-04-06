package com.mockproject.notary_admin_server.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.mockproject.notary_common.constant.EmploymentType;

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
public class AdminInviteNotaryRequest {
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    String email;

    @NotBlank(message = "SSN không được để trống")
    @Size(max = 32, message = "SSN không được vượt quá 32 ký tự")
    String ssn;

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 64, message = "Họ và tên không được vượt quá 64 ký tự")
    String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
    String phone;

    LocalDate dateOfBirth;

    @Size(max = 128, message = "Địa chỉ không được vượt quá 128 ký tự")
    String address;

    EmploymentType employmentType;

    LocalDate startDate;

    @Size(max = 1000, message = "Ghi chú nội bộ không được vượt quá 1000 ký tự")
    String internalNotes;
}
