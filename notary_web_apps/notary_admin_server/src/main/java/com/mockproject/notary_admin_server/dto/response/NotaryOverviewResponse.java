package com.mockproject.notary_admin_server.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.mockproject.notary_common.constant.DocCategory;
import com.mockproject.notary_common.constant.VerifiedStatus;


/**
 * NotaryOverviewResponse
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 26-03-2026      DangQuoc      create
 * 28-03-2026      DangQuoc      edit
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotaryOverviewResponse {
    @JsonProperty("commission")
    private Commission commission;
    @JsonProperty("bond")
    private Bond bond;
    @JsonProperty("eo_insurance")
    private EoInsurance eoInsurance;
    @JsonProperty("document")
    private Document document;
    @JsonProperty("contact_information")
    private ContactInformation contactInformation;
    @JsonProperty("service_areas")
    private List<String> serviceAreas;

    @AllArgsConstructor
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Commission {
        private String status;
        @JsonProperty("expiration_date")
        private LocalDate expirationDate;
        @JsonProperty("expected_renewal_date")
        private LocalDate expectedRenewalDate;
        @JsonProperty("is_renewal_applied")
        private Boolean isRenewalApplied;
    }

    @AllArgsConstructor
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Bond {
        private String status;
        @JsonProperty("bond_amount")
        private BigDecimal bond_amount;
    }

    @AllArgsConstructor
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EoInsurance {
        private String status;
        @JsonProperty("expiration_date")
        private LocalDate expirationDate;
        @JsonProperty("effective_date")
        private LocalDate effectiveDate;
    }

    @AllArgsConstructor
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Document {
        @JsonProperty("doc_category")
        private DocCategory docCategory;
        @JsonProperty("verified_status")
        private VerifiedStatus verifiedStatus;
        @JsonProperty("upload_date")
        private LocalDateTime uploadDate;
    }

    @AllArgsConstructor
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ContactInformation {
        private String email;
        private String phone;
        private String address;
    }
}