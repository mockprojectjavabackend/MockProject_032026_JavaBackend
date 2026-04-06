package com.mockproject.notary_admin_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;


/**
 * StateResponse
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 30-03-2026      PhamTam      create
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StateResponse {
    UUID stateId;
    String stateName;
    String stateCode;
}
