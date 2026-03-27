package com.mockproject.notary_admin_server.dto.response;

import com.mockproject.notary_common.constant.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * NotaryDetailResponse
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 26-03-2026      DangQuoc      create
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotaryDetailResponse {
    private String photoUrl;
    private String fullName;
    private String commissionNumber;
    private String email;
    private String phone;
    private String address;
    private UserStatus status;
}
