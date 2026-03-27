package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.entity.FederalHoliday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FederalHolidayRepository extends JpaRepository<FederalHoliday, UUID> {
}
