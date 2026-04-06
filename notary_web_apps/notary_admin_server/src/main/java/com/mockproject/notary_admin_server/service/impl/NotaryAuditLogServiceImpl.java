package com.mockproject.notary_admin_server.service.impl;

import com.mockproject.notary_admin_server.dto.response.AuditTrailDetailResponse;
import com.mockproject.notary_admin_server.dto.response.AuditTrailPageResponse;
import com.mockproject.notary_admin_server.dto.response.AuditTrailResponse;
import com.mockproject.notary_admin_server.dto.response.MetaResponse;
import com.mockproject.notary_admin_server.exception.AppException;
import com.mockproject.notary_admin_server.exception.errorCode.AuditErrorCode;
import com.mockproject.notary_admin_server.repository.NotaryAuditLogRepository;
import com.mockproject.notary_admin_server.repository.UserRepository;
import com.mockproject.notary_admin_server.service.NotaryAuditLogService;
import com.mockproject.notary_common.constant.AuditLogAction;
import com.mockproject.notary_common.entity.notary.NotaryAuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotaryAuditLogServiceImpl implements NotaryAuditLogService {

    private final NotaryAuditLogRepository notaryAuditLogRepository;
    private final UserRepository userRepository; // để lấy tên admin

    @Override
    public AuditTrailPageResponse getAuditTrail(
            UUID notaryId, String timeRange, UUID userId, String action, int page, int limit) {

        LocalDateTime fromTime = calculateFromTime(timeRange);

        // Parse action string → enum (null nếu không filter)
        AuditLogAction auditAction = null;
        if (action != null && !action.isBlank()) {
            try {
                auditAction = AuditLogAction.valueOf(action.toUpperCase());
            } catch (IllegalArgumentException e) {
                auditAction = null;
            }
        }

        List<NotaryAuditLog> logs = notaryAuditLogRepository
                .findByNotaryIdWithFilters(
                        notaryId, fromTime, userId, auditAction,
                        PageRequest.of(page - 1, limit));

        long total = notaryAuditLogRepository
                .countByNotaryIdWithFilters(notaryId, fromTime, userId, auditAction);

        List<AuditTrailResponse> data = logs.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return AuditTrailPageResponse.builder()
                .data(data)
                .meta(MetaResponse.builder().total(total).build())
                .build();
    }

    /**
     * Get detailed audit trail entry by ID
     *
     * @param notaryId UUID of the notary
     * @param auditId  UUID of the audit log entry
     * @return detailed audit trail response
     */
    @Override
    public AuditTrailDetailResponse getAuditTrailDetail(UUID notaryId, UUID auditId) {
        NotaryAuditLog log = notaryAuditLogRepository
                .findByIdAndNotaryId(auditId, notaryId)
                .orElseThrow(() -> new AppException(AuditErrorCode.AUDIT_LOG_NOT_FOUND));

        String adminName = resolveAdminName(log.getChangeByUserId());

        return AuditTrailDetailResponse.builder()
                .id(log.getId().toString())
                .timestamp(resolveTimestamp(log))
                .action(log.getAction().name())
                .tableName(log.getTableName())
                .administrator(adminName)
                .beforeValue(log.getOldValue())
                .afterValue(log.getNewValue())
                .build();
    }

    // Tính thời gian bắt đầu lọc dựa theo timeRange
    private LocalDateTime calculateFromTime(String timeRange) {
        return switch (timeRange) {
            case "last_day" -> LocalDateTime.now().minusDays(1);
            case "last_week" -> LocalDateTime.now().minusWeeks(1);
            case "last_month" -> LocalDateTime.now().minusMonths(1);
            default -> LocalDateTime.now().minusDays(1);
        };
    }

    // Map entity → DTO
    private AuditTrailResponse mapToResponse(NotaryAuditLog log) {

        // Lấy tên admin từ changeByUserId
        String adminName = userRepository.findById(log.getChangeByUserId())
                .map(user -> user.getFullName()) // xem entity User có getFullName không
                .orElse("Unknown");

        // Parse oldValue và newValue để lấy field_changed, before, after
        String fieldChanged = extractField(log.getOldValue(), log.getNewValue());
        String beforeValue = extractValue(log.getOldValue());
        String afterValue = extractValue(log.getNewValue());

        return AuditTrailResponse.builder()
                .timestamp(log.getCreatedAt()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .action(log.getAction().name())
                .administrator(adminName)
                .fieldChanged(fieldChanged)
                .beforeValue(beforeValue)
                .afterValue(afterValue)
                .build();
    }

    // Lấy tên field từ oldValue hoặc newValue
    // oldValue = {"phone": "000"} → trả về "phone"
    private String extractField(Map<String, Object> oldValue,
            Map<String, Object> newValue) {
        if (oldValue != null && !oldValue.isEmpty()) {
            return oldValue.keySet().iterator().next();
        }
        if (newValue != null && !newValue.isEmpty()) {
            return newValue.keySet().iterator().next();
        }
        return "";
    }

    // Lấy giá trị từ map
    // {"phone": "000"} → trả về "000"
    private String extractValue(Map<String, Object> valueMap) {
        if (valueMap == null || valueMap.isEmpty())
            return "";
        Object value = valueMap.values().iterator().next();
        return value != null ? value.toString() : "";
    }

    /**
     * Resolve admin full name from user ID
     *
     * @param userId UUID of the user who performed the action
     * @return full name of the admin or "Unknown" if not found
     */
    private String resolveAdminName(UUID userId) {
        if (userId == null)
            return "Unknown";
        return userRepository.findById(userId)
                .map(user -> user.getFullName())
                .orElse("Unknown");
    }

    /**
     * Resolve timestamp from audit log
     *
     * @param log audit log entity
     * @return formatted timestamp string or "N/A" if null
     */
    private String resolveTimestamp(NotaryAuditLog log) {
        if (log.getCreatedAt() != null) {
            return log.getCreatedAt()
                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        return "N/A";
    }

}