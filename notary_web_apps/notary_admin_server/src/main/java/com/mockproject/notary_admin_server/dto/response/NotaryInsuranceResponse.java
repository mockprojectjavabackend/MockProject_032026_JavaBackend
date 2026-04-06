package com.mockproject.notary_admin_server.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mockproject.notary_common.entity.notary.NotaryInsurance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotaryInsuranceResponse(
        UUID id,
        @JsonProperty("notary_id") UUID notaryId,
        @JsonProperty("provider_name") String providerName,
        @JsonProperty("policy_number") String policyNumber,
        @JsonProperty("coverage_amount") BigDecimal coverageAmount,
        @JsonProperty("effective_date") LocalDate effectiveDate,
        @JsonProperty("expiration_date") LocalDate expirationDate,
        @JsonProperty("file_url") String fileUrl) {

    public static NotaryInsuranceResponse fromEntity(NotaryInsurance insurance) {
        return new NotaryInsuranceResponse(
                insurance.getId(),
                insurance.getNotary() != null ? insurance.getNotary().getId() : null,
                insurance.getProviderName(),
                insurance.getPolicyNumber(),
                insurance.getCoverageAmount(),
                insurance.getEffectiveDate(),
                insurance.getExpirationDate(),
                insurance.getFileUrl());
    }
}