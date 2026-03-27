package com.mockproject.notary_admin_server.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateNotaryBondRequest(
                @JsonProperty("provider_name") String providerName,
                @JsonProperty("bond_amount") BigDecimal bondAmount,
                @JsonProperty("effective_date") LocalDate effectiveDate,
                @JsonProperty("expiration_date") LocalDate expirationDate,
                @JsonProperty("file_url") String fileUrl) {
}