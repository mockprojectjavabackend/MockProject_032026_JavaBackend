package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_common.entity.FederalHoliday;

import java.util.Set;

public interface IFederalHolidayService {
    Set<FederalHoliday> getFederalHolidays();
}
