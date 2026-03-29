package com.mockproject.notary_admin_server.service.impl;

import com.mockproject.notary_admin_server.dto.response.RecentActivityResponse;
import com.mockproject.notary_admin_server.repository.NotaryAuditLogRepository;
import com.mockproject.notary_admin_server.repository.UserRepository;
import com.mockproject.notary_admin_server.service.NotaryActivityService;
import com.mockproject.notary_common.constant.AuditLogAction;
import com.mockproject.notary_common.entity.notary.NotaryAuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotaryActivityServiceImpl implements NotaryActivityService {

    private final NotaryAuditLogRepository notaryAuditLogRepository;
    private final UserRepository userRepository;

    @Override
    public List<RecentActivityResponse> getRecentActivities(UUID notaryId, int limit) {

        // Query N bản ghi mới nhất từ audit_logs
        List<NotaryAuditLog> logs = notaryAuditLogRepository
                .findRecentActivities(
                        notaryId,
                        PageRequest.of(0, limit) // luôn lấy trang đầu tiên
                );

        return logs.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private RecentActivityResponse mapToResponse(NotaryAuditLog log) {

        // Thử đơn giản hóa, không đụng vào oldValue/newValue
        String actionType = log.getTableName() != null
                ? log.getTableName()
                : "Unknown";

        String performedBy = userRepository
                .findById(log.getChangeByUserId())
                .map(user -> user.getFullName())
                .orElse("Unknown");

        return RecentActivityResponse.builder()
                .actionType(actionType)
                .description("Activity recorded")
                .performedBy(performedBy)
                .timestamp(log.getCreatedAt() != null
                        ? log.getCreatedAt().toString()
                        : "")
                .build();
    }

    // Map tableName + action → actionType
    // FE dùng actionType này để render đúng icon (Edit, Cờ, Document)
    private String mapActionType(String tableName, AuditLogAction action) {
        if (tableName == null)
            return "Unknown";

        return switch (tableName) {
            case "notaries" -> action == AuditLogAction.UPDATE
                    ? "Profile Updated"
                    : "Profile Created";

            case "notary_status_histories" -> "Status Changed";

            case "notary_documents" -> action == AuditLogAction.INSERT
                    ? "Document Uploaded"
                    : "Document Updated";

            case "notary_commissions" -> "Commission Updated";

            default -> action.name(); // fallback nếu bảng chưa map
        };
    }

    // Tạo description mô tả chi tiết hành động
    private String buildDescription(String actionType, NotaryAuditLog log) {
        return switch (actionType) {
            case "Profile Updated" -> "Contact information modified by admin";
            case "Status Changed" -> "Notary status has been updated";
            case "Document Uploaded" -> "New document uploaded to the system";
            case "Commission Updated" -> "Commission information has been updated";
            default -> "System activity recorded";
        };
    }
}
