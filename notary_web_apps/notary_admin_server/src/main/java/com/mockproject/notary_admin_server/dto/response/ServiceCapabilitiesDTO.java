package com.mockproject.notary_admin_server.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;

import java.util.UUID;

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
