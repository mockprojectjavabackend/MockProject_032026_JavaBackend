package com.mockproject.notary_admin_server.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mockproject.notary_common.constant.DocCategory;
import com.mockproject.notary_common.constant.VerifiedStatus;
import lombok.*;

/**
 * NotaryOverviewResponse
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 26-03-2026      DangQuoc      create
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
    private ContactInpormation contactInformation;
    @JsonProperty("service_areas")
    private List<String> serviceAreas;

    @Getter
    @Setter
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

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Bond {
        private String status;
        @JsonProperty("bond_amount")
        private BigDecimal bond_amount;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EoInsurance {
        private String status;
        @JsonProperty("expiration_date")
        private LocalDate expirationDate;
        @JsonProperty("effective_date")
        private LocalDate effectiveDate;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Document {
        @JsonProperty("doc_category")
        private DocCategory docCategory;
        @JsonProperty("verified_status")
        private VerifiedStatus verifiedStatus;
        @JsonProperty("upload_date")
        private LocalDateTime uploadDate;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ContactInpormation {
        private String email;
        private String phone;
        private String address;
    }
}