package com.mockproject.notary_admin_server.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.mockproject.notary_common.constant.UserStatus;

/**
 * NotaryStatusResponse
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 26-03-2026      DangQuoc      create
 * 29-03-2026      DangQuoc      edit
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotaryStatusResponse {
    private UUID id;
    private UserStatus status;
    @JsonProperty("update_at")
    private LocalDateTime updatedAt;
}
