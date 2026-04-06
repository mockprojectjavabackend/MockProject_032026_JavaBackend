package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.entity.notary.NotaryAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotaryAuditLogRepository extends JpaRepository<NotaryAuditLog, UUID> {
}
