package com.mockproject.notary_admin_server.service.impl;

import com.mockproject.notary_admin_server.repository.FederalHolidayRepository;
import com.mockproject.notary_admin_server.service.IFederalHolidayService;
import com.mockproject.notary_common.entity.FederalHoliday;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * FederalHolidayService
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      ThoHa       create
 */

@Service
@RequiredArgsConstructor
public class FederalHolidayService implements IFederalHolidayService {

    private final FederalHolidayRepository federalHolidayRepository;

    @Override
    public Set<FederalHoliday> getFederalHolidays() {
        return new HashSet<>(federalHolidayRepository.findAll());
    }
}
