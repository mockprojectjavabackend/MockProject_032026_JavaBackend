package com.mockproject.notary_admin_server.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import com.mockproject.notary_common.constant.EmploymentType;


/**
 * NotaryAdminResponse
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 30-03-2026      PhamTam      create
 * 02-04-2026      PhamTam      edit
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class NotaryAdminResponse extends NotaryBaseResponse  {
    String ssn;
    EmploymentType employmentType;
    String internalNotes;
}
