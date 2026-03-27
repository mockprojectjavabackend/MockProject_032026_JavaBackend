package com.mockproject.notary_admin_server.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateNotaryInsuranceRequest(
        @JsonProperty("provider_name") String providerName,
        @JsonProperty("policy_number") String policyNumber,
        @JsonProperty("coverage_amount") BigDecimal coverageAmount,
        @JsonProperty("expiration_date") LocalDate expirationDate) {
}