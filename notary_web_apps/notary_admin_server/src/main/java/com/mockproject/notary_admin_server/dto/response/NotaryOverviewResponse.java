package com.mockproject.notary_admin_server.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
public class NotaryOverviewResponse {
    private Commission commission;
    private Bond bond;
    private EoInsurance eoInsurance;
    private Document document;
    private ContactInpormation contactInformation;
    private List<String> serviceAreas;

    @Getter
    @Setter
    public static class Commission {
        private String status;
        private String expires;
        private Boolean isRenewalApplied;
    }

    @Getter
    @Setter
    public static class Bond {
        private String status;
        private BigDecimal coverage;
    }

    @Getter
    @Setter
    public static class EoInsurance {
        private String status;
        private String expires;
    }

    @Getter
    @Setter
    public static class Document {
        private DocCategory docCategory;
        private VerifiedStatus verifiedStatus;
        private LocalDateTime uploadDate;
    }

    @Getter
    @Setter
    public static class ContactInpormation {
        private String email;
        private String phone;
        private String address;
    }
}