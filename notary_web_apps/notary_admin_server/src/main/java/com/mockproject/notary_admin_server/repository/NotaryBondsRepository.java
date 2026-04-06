package com.mockproject.notary_admin_server.repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.mockproject.notary_common.entity.notary.NotaryBonds;

@Repository
public interface NotaryBondsRepository extends JpaRepository<NotaryBonds, UUID>, JpaSpecificationExecutor<NotaryBonds> {

	Optional<NotaryBonds> findFirstByNotary_IdOrderByCreatedAtDesc(UUID notaryId);

	List<NotaryBonds> findAllByNotary_IdOrderByCreatedAtDesc(UUID notaryId);

}
