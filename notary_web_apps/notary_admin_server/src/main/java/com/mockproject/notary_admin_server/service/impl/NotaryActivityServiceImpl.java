package com.mockproject.notary_admin_server.service.impl;

import com.mockproject.notary_admin_server.dto.response.RecentActivityResponse;
import com.mockproject.notary_admin_server.exception.AppException;
import com.mockproject.notary_admin_server.exception.errorCode.AuditErrorCode;
import com.mockproject.notary_admin_server.mapper.AuditServiceHelper;
import com.mockproject.notary_admin_server.repository.NotaryAuditLogRepository;
import com.mockproject.notary_admin_server.repository.NotaryRepository;
import com.mockproject.notary_admin_server.repository.UserRepository;
import com.mockproject.notary_admin_server.service.NotaryActivityService;
import com.mockproject.notary_common.constant.AuditLogAction;
import com.mockproject.notary_common.entity.notary.NotaryAuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotaryActivityServiceImpl implements NotaryActivityService {

        private final NotaryAuditLogRepository notaryAuditLogRepository;
        private final NotaryRepository notaryRepository;
        private final UserRepository userRepository;
        private final AuditServiceHelper auditServiceHelper;

        /**
         * Get recent activities of a notary for Recent Audit Log block (right side)
         *
         * @param notaryId UUID of the notary
         * @param limit    number of records to return
         * @return list of recent activity responses
         */
        @Override
        public List<RecentActivityResponse> getRecentActivities(UUID notaryId, int limit) {
                // Validate notary exists
                notaryRepository.findById(notaryId)
                                .orElseThrow(() -> new AppException(AuditErrorCode.NOTARY_NOT_FOUND));

                // Query recent audit logs
                List<NotaryAuditLog> logs = notaryAuditLogRepository
                                .findRecentActivities(notaryId, PageRequest.of(0, limit));

                return logs.stream()
                                .map(this::mapToActivityResponse)
                                .collect(Collectors.toList());
        }

        /**
         * Map NotaryAuditLog entity to RecentActivityResponse DTO
         *
         * @param log audit log entity
         * @return mapped response DTO
         */
        private RecentActivityResponse mapToActivityResponse(NotaryAuditLog log) {
                String actionType = resolveActionType(log.getTableName(), log.getAction());
                String description = resolveDescription(actionType);
                String performedBy = auditServiceHelper.resolveAdminName(log.getChangeByUserId());
                String timestamp = auditServiceHelper.resolveTimestamp(log.getCreatedAt());
                String documentName = resolveEntityName(log);

                return RecentActivityResponse.builder()
                                .actionType(actionType)
                                .description(description)
                                .performedBy(performedBy)
                                .timestamp(timestamp)
                                .documentName(documentName)
                                .build();
        }

        /**
         * Resolve action type based on table name and action
         * Used by FE to render correct icon (Edit, Flag, Document)
         *
         * @param tableName name of the affected table
         * @param action    audit log action (INSERT/UPDATE/DELETE)
         * @return human-readable action type string
         */
        private String resolveActionType(String tableName, AuditLogAction action) {
                if (tableName == null || action == null) {
                        return "Unknown";
                }

                return switch (tableName) {
                        case "notaries" -> action == AuditLogAction.UPDATE
                                        ? "Profile Updated"
                                        : "Profile Created";
                        case "notary_status_histories" -> "Status Changed";
                        case "notary_documents" -> action == AuditLogAction.INSERT
                                        ? "Document Uploaded"
                                        : "Document Updated";
                        case "notary_commissions" -> "Commission Updated";
                        default -> action.name();
                };
        }

        /**
         * Resolve description message based on action type
         *
         * @param actionType resolved action type string
         * @return description message
         */
        private String resolveDescription(String actionType) {
                return switch (actionType) {
                        case "Profile Updated" -> "Contact information modified by admin";
                        case "Profile Created" -> "Notary profile has been created";
                        case "Status Changed" -> "Notary status has been updated";
                        case "Document Uploaded" -> "New document uploaded to the system";
                        case "Document Updated" -> "Document information has been updated";
                        case "Commission Updated" -> "Commission information has been updated";
                        default -> "System activity recorded";
                };
        }

        private String resolveEntityName(NotaryAuditLog log) {
                if (log.getTableName() == null)
                        return null;

                return switch (log.getTableName()) {
                        case "notary_documents" -> extractEntityName(log.getNewValue(), "document_name");
                        case "notary_commissions" -> extractEntityName(log.getNewValue(), "commission_type");
                        default -> null;
                };
        }

        /**
         * Extract specific field value from JSON map
         */
        private String extractEntityName(Map<String, Object> valueMap, String fieldName) {
                if (valueMap == null || valueMap.isEmpty())
                        return null;
                Object value = valueMap.get(fieldName);
                return value != null ? value.toString() : null;
        }
}