package com.mockproject.notary_admin_server.dto.response.capability;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

/**
 * ServiceCapabilitiesDTO
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      ThoHa       create
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ServiceCapabilitiesDTO {
    private UUID id;
    @JsonProperty("mobile_notary")
    private boolean mobile ;
    private boolean ron ;
    @JsonProperty("loan_signing")
    private boolean loanSigning ;
    @JsonProperty("apostille_support")
    private boolean apostilleRelatedSupport;

}
