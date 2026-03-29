package com.mockproject.notary_admin_server.service.impl;

import com.mockproject.notary_admin_server.dto.response.AuditTrailResponse;
import com.mockproject.notary_admin_server.repository.NotaryAuditLogRepository;
import com.mockproject.notary_admin_server.repository.UserRepository;
import com.mockproject.notary_admin_server.service.NotaryAuditLogService;
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
    public List<AuditTrailResponse> getAuditTrail(
            UUID notaryId, String timeRange, int page, int limit) {

        // Bước 1: Tính fromTime dựa theo timeRange
        LocalDateTime fromTime = calculateFromTime(timeRange);

        // Bước 2: Query database
        List<NotaryAuditLog> logs = notaryAuditLogRepository
                .findByNotaryIdAndTimeRange(
                        notaryId,
                        fromTime,
                        PageRequest.of(page - 1, limit) // page bắt đầu từ 0 trong Spring
                );

        // Bước 3: Map từng log → AuditTrailResponse
        return logs.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
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
}