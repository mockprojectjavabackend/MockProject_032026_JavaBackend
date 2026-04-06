package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_common.entity.FederalHoliday;

import java.util.Set;

/**
 * IFederalHolidayService
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      ThoHa       create
 */

public interface IFederalHolidayService {
    Set<FederalHoliday> getFederalHolidays();
}
