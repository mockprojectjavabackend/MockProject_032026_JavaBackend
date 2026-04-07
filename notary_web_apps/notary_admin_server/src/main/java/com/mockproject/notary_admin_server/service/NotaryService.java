package com.mockproject.notary_admin_server.service;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.request.NotaryCreateRequestDTO;
import com.mockproject.notary_admin_server.dto.request.NotaryUpdateRequestDTO;
import com.mockproject.notary_admin_server.dto.response.*;
import com.mockproject.notary_admin_server.exception.AppException;
import com.mockproject.notary_admin_server.exception.errorCode.BaseErrorCode;
import com.mockproject.notary_admin_server.exception.errorCode.NotaryErrorCode;
import com.mockproject.notary_admin_server.exception.errorCode.UserErrorCode;
import com.mockproject.notary_admin_server.repository.NotaryAuditLogRepository;
import com.mockproject.notary_admin_server.repository.NotaryRepository;
import com.mockproject.notary_admin_server.repository.NotarySpecification;
import com.mockproject.notary_admin_server.repository.UserRepository;
import com.mockproject.notary_common.constant.AuditLogAction;
import com.mockproject.notary_common.entity.User;
import com.mockproject.notary_common.entity.notary.Notary;
import com.mockproject.notary_common.entity.notary.NotaryAuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * NotaryService
 *
 * @version 1.1
 *
 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      TranMinh    create
 * 29-03-2026      TranMinh    modify
 */
@Service
@RequiredArgsConstructor
public class NotaryService {
    private final NotaryRepository notaryRepository;
    private final UserRepository userRepository;
    private final NotaryAuditLogRepository auditLogRepository;

    @Transactional(readOnly = true)
    public ApiSuccessResponse<PagedResponse<NotaryResponseDTO>> getNotaries(
            String status, String state, String serviceType, String search, int page, int limit) {

        Pageable pageable = PageRequest.of(Math.max(0, page - 1), limit);

        Page<Notary> notaryPage = notaryRepository.findAll(
                NotarySpecification.filter(status, state, serviceType, search),
                pageable
        );

        List<NotaryResponseDTO> data = notaryPage.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        PagedResponse<NotaryResponseDTO> pagedData = PagedResponse.<NotaryResponseDTO>builder()
                .items(data)
                .total(notaryPage.getTotalElements())
                .page(page)
                .limit(limit)
                .totalPages(notaryPage.getTotalPages())
                .build();

        return ApiSuccessResponse.ok(pagedData);
    }

    private NotaryResponseDTO mapToDTO(Notary notary) {
        return NotaryResponseDTO.builder()
                .id(notary.getId())
                .userId(notary.getUser() != null ? notary.getUser().getId() : null)
                .fullName(notary.getFullName())
                .photoUrl(notary.getPhotoUrl())
                .phone(notary.getPhone())
                .employmentType(notary.getEmploymentType() != null ? notary.getEmploymentType().name() : null)
                .startDate(notary.getStartDate())
                .residentialAddress(notary.getAddress())
                .build();
    }

    @Transactional(readOnly = true)
    public ApiSuccessResponse<NotaryDetailResponseDTO> getNotaryDetail(UUID notaryId) {
        Notary notary = notaryRepository.findById(notaryId)
                .orElseThrow(() -> new AppException(BaseErrorCode.NOTARY_NOT_FOUND));

        boolean isAdmin = checkAdminRole();

        NotaryDetailResponseDTO data = mapToDetailDTO(notary, isAdmin);

        return ApiSuccessResponse.ok(data);
    }

    /**
     * Kiểm tra xem người dùng hiện tại có role ADMIN hay không
     * thông qua SecurityContextHolder (JWT claims).
     */
    private boolean checkAdminRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equalsIgnoreCase("ROLE_ADMIN") || role.equalsIgnoreCase("admin"));
    }

    private NotaryDetailResponseDTO mapToDetailDTO(Notary notary, boolean isAdmin) {
        String ssn = notary.getSsn();
        String internalNotes = notary.getInternalNotes();

        if (!isAdmin) {
            ssn = maskSsn(ssn);
            internalNotes = null;
        }

        return NotaryDetailResponseDTO.builder()
                .id(notary.getId())
                .userId(notary.getUser() != null ? notary.getUser().getId() : null)
                .ssn(ssn)
                .fullName(notary.getFullName())
                .dateOfBirth(notary.getDateOfBirth())
                .photoUrl(notary.getPhotoUrl())
                .phone(notary.getPhone())
                .employmentType(notary.getEmploymentType() != null ? notary.getEmploymentType().name() : null)
                .startDate(notary.getStartDate())
                .internalNotes(internalNotes)
                .residentialAddress(notary.getAddress())
                .build();
    }

    private String maskSsn(String ssn) {
        if (ssn == null || ssn.length() <= 4) {
            return "***-**-****";
        }
        String last4 = ssn.substring(ssn.length() - 4);
        return "***-**-" + last4;
    }

    @Transactional
    public ApiSuccessResponse<NotaryCreateResponseDTO> createNotary(NotaryCreateRequestDTO request) {

        if (notaryRepository.existsByUser_Id(request.getUserId())) {
            throw new AppException(NotaryErrorCode.NOTARY_ALREADY_EXISTS);
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

        String safeInternalNotes = null;
        if (checkAdminRole()) {
            safeInternalNotes = request.getInternalNotes();
        }

        Notary newNotary = Notary.builder()
                .user(user)
                .ssn(request.getSsn())
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .photoUrl(request.getPhotoUrl())
                .phone(request.getPhone())
                .employmentType(request.getEmploymentType())
                .startDate(request.getStartDate())
                .internalNotes(safeInternalNotes)
                .address(request.getResidentialAddress())
                .build();

        Notary savedNotary = notaryRepository.save(newNotary);

        saveAuditLog(newNotary, AuditLogAction.INSERT, Collections.emptyMap(), extractState(newNotary));

        NotaryCreateResponseDTO responseData = NotaryCreateResponseDTO.builder()
                .id(savedNotary.getId())
                .userId(savedNotary.getUser().getId())
                .fullName(savedNotary.getFullName())
                .employmentType(savedNotary.getEmploymentType().name())
                .startDate(savedNotary.getStartDate())
                .build();

        return ApiSuccessResponse.ok(responseData);
    }

    @Transactional
    public ApiSuccessResponse<NotaryUpdateResponseDTO> updateNotary(UUID notaryId, NotaryUpdateRequestDTO request) {

        Notary notary = notaryRepository.findById(notaryId)
                .orElseThrow(() -> new AppException(BaseErrorCode.NOTARY_NOT_FOUND));

        List<String> updatedFields = new ArrayList<>();
        boolean isAdmin = checkAdminRole();
        Map<String, Object> oldState = extractState(notary);

        if (request.getFullName() != null && !request.getFullName().equals(notary.getFullName())) {
            notary.setFullName(request.getFullName());
            updatedFields.add("fullName");
        }

        if (request.getPhotoUrl() != null && !request.getPhotoUrl().equals(notary.getPhotoUrl())) {
            notary.setPhotoUrl(request.getPhotoUrl());
            updatedFields.add("photoUrl");
        }

        if (request.getPhone() != null && !request.getPhone().equals(notary.getPhone())) {
            notary.setPhone(request.getPhone());
            updatedFields.add("phone");
        }

        if (request.getEmploymentType() != null && request.getEmploymentType() != notary.getEmploymentType()) {
            notary.setEmploymentType(request.getEmploymentType());
            updatedFields.add("employmentType");
        }

        if (request.getStartDate() != null && !request.getStartDate().equals(notary.getStartDate())) {
            notary.setStartDate(request.getStartDate());
            updatedFields.add("startDate");
        }

        if (request.getResidentialAddress() != null && !request.getResidentialAddress().equals(notary.getAddress())) {
            notary.setAddress(request.getResidentialAddress());
            updatedFields.add("residentialAddress");
        }

        if (request.getInternalNotes() != null && !request.getInternalNotes().equals(notary.getInternalNotes())) {
            if (isAdmin) {
                notary.setInternalNotes(request.getInternalNotes());
                updatedFields.add("internalNotes");
            }
        }

        notaryRepository.save(notary);

        Map<String, Object> newState = extractState(notary);
        saveAuditLog(notary, AuditLogAction.UPDATE, oldState, newState);

        NotaryUpdateResponseDTO responseData = NotaryUpdateResponseDTO.builder()
                .id(notary.getId())
                .updatedFields(updatedFields)
                .updatedAt(notary.getUpdatedAt() != null ? notary.getUpdatedAt() : LocalDateTime.now())
                .build();

        return ApiSuccessResponse.ok(responseData);
    }

    @Transactional
    public ApiSuccessResponse<Void> deleteNotary(UUID notaryId) {

        Notary notary = notaryRepository.findById(notaryId)
                .orElseThrow(() -> new AppException(BaseErrorCode.NOTARY_NOT_FOUND));

        Map<String, Object> oldState = extractState(notary);

        // Soft-delete: ghi thời điểm xóa
        notary.setDeletedAt(LocalDateTime.now());
        notaryRepository.save(notary);

        Map<String, Object> newState = extractState(notary);
        saveAuditLog(notary, AuditLogAction.DELETE, oldState, newState);

        return ApiSuccessResponse.deleted();
    }

    private void saveAuditLog(Notary notary, AuditLogAction action, Map<String, Object> oldValue, Map<String, Object> newValue) {
        UUID currentUserId = getCurrentUserId();

        NotaryAuditLog auditLog = NotaryAuditLog.builder()
                .tableName("notaries")
                .recordId(notary.getId())
                .action(action)
                .oldValue(oldValue)
                .newValue(newValue)
                .changeByUserId(currentUserId)
                .notary(notary)
                .build();

        auditLogRepository.save(auditLog);
    }

    private Map<String, Object> extractState(Notary notary) {
        Map<String, Object> state = new HashMap<>();
        state.put("fullName", notary.getFullName());
        state.put("phone", notary.getPhone());
        state.put("employmentType", notary.getEmploymentType() != null ? notary.getEmploymentType().name() : null);
        state.put("address", notary.getAddress());
        state.put("internalNotes", notary.getInternalNotes());
        return state;
    }

    /**
     * Lấy userId của người dùng hiện tại từ JWT token qua SecurityContextHolder.
     * Trả về null nếu không có session hợp lệ (cho phép audit log ghi null).
     */
    private UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof Jwt jwt) {
            String subject = jwt.getSubject();
            if (subject != null) {
                try {
                    return UUID.fromString(subject);
                } catch (IllegalArgumentException ignored) {
                    // subject không phải UUID format
                }
            }
        }
        return null;
    }
}