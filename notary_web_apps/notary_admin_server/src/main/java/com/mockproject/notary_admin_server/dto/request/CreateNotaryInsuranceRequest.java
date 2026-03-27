package com.mockproject.notary_admin_server.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateNotaryInsuranceRequest(
                @JsonProperty("provider_name") @NotBlank(message = "provider_name is required") String providerName,
                @JsonProperty("policy_number") @NotBlank(message = "policy_number is required") String policyNumber,
                @JsonProperty("coverage_amount") @NotNull(message = "coverage_amount is required") BigDecimal coverageAmount,
                @JsonProperty("expiration_date") @NotNull(message = "expiration_date is required") LocalDate expirationDate) {
}