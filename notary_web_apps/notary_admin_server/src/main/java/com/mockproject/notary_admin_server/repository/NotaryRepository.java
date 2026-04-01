package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.entity.notary.Notary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotaryRepository extends JpaRepository<Notary, UUID> {
}