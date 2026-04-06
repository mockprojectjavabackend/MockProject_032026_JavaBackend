package com.mockproject.notary_admin_server.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateNotaryBondRequest(
        @JsonProperty("provider_name") @NotBlank(message = "provider_name is required") String providerName,
        @JsonProperty("bond_amount") @NotNull(message = "bond_amount is required") BigDecimal bondAmount,
        @JsonProperty("effective_date") @NotNull(message = "effective_date is required") LocalDate effectiveDate,
        @JsonProperty("expiration_date") @NotNull(message = "expiration_date is required") LocalDate expirationDate,
        @JsonProperty("file_url") @NotBlank(message = "file_url is required") String fileUrl) {
}