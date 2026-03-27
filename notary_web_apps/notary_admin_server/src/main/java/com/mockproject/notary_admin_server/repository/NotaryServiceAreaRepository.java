package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.entity.notary.NotaryServiceArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotaryServiceAreaRepository extends JpaRepository<NotaryServiceArea, UUID> {
    NotaryServiceArea findByNotary_Id(UUID id);
    NotaryServiceArea findByCountyName(String countyName);
}
