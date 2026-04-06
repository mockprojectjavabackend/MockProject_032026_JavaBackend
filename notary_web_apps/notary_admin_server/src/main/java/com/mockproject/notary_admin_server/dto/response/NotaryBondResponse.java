package com.mockproject.notary_admin_server.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mockproject.notary_common.entity.notary.NotaryBonds;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotaryBondResponse(
        UUID id,
        @JsonProperty("notary_id") UUID notaryId,
        @JsonProperty("provider_name") String providerName,
        @JsonProperty("bond_amount") BigDecimal bondAmount,
        @JsonProperty("effective_date") LocalDate effectiveDate,
        @JsonProperty("expiration_date") LocalDate expirationDate,
        @JsonProperty("file_url") String fileUrl) {

    public static NotaryBondResponse fromEntity(NotaryBonds bond) {
        return new NotaryBondResponse(
                bond.getId(),
                bond.getNotary() != null ? bond.getNotary().getId() : null,
                bond.getProviderName(),
                bond.getBondAmount(),
                bond.getEffectiveDate(),
                bond.getExpirationDate(),
                bond.getFileUrl());
    }
}