package com.mockproject.notary_admin_server.service.impl;

import com.mockproject.notary_admin_server.dto.request.AdminInviteNotaryRequest;
import com.mockproject.notary_admin_server.dto.response.NotaryInviteResponse;
import com.mockproject.notary_admin_server.exception.AppException;
import com.mockproject.notary_admin_server.exception.errorCode.NotaryErrorCode;
import com.mockproject.notary_admin_server.exception.errorCode.RoleErrorCode;
import com.mockproject.notary_admin_server.exception.errorCode.UserErrorCode;
import com.mockproject.notary_admin_server.repository.NotaryRepository;
import com.mockproject.notary_admin_server.repository.RoleRepository;
import com.mockproject.notary_admin_server.repository.UserInvitationTokenRepository;
import com.mockproject.notary_admin_server.repository.UserRepository;
import com.mockproject.notary_admin_server.service.AdminNotaryService;
import com.mockproject.notary_admin_server.service.EmailService;
import com.mockproject.notary_common.constant.PredefinedRole;
import com.mockproject.notary_common.constant.UserStatus;
import com.mockproject.notary_common.entity.Role;
import com.mockproject.notary_common.entity.User;
import com.mockproject.notary_common.entity.UserInvitationToken;
import com.mockproject.notary_common.entity.notary.Notary;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j(topic = "ADMIN-NOTARY-SERVICE")
public class AdminNotaryServiceImpl implements AdminNotaryService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    NotaryRepository notaryRepository;
    UserInvitationTokenRepository invitationTokenRepository;
    EmailService emailService;

    @lombok.experimental.NonFinal
    @Value("${app.invite.base-url}")
    String inviteBaseUrl;

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public NotaryInviteResponse inviteNotary(AdminInviteNotaryRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new AppException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (notaryRepository.existsBySsn(request.getSsn().trim())) {
            throw new AppException(NotaryErrorCode.SSN_ALREADY_EXISTS);
        }

        Role notaryRole = roleRepository.findByRoleName(PredefinedRole.NOTARY)
                .orElseThrow(() -> new AppException(RoleErrorCode.ROLE_NOT_FOUND));

        User user = User.builder()
                .email(email)
                .passwordHash(null)
                .status(UserStatus.INACTIVE)
                .roles(Set.of(notaryRole))
                .build();

        user = userRepository.save(user);

        Notary notary = Notary.builder()
                .ssn(request.getSsn().trim())
                .fullName(request.getFullName().trim())
                .phone(request.getPhone().trim())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress() == null ? null : request.getAddress().trim())
                .employmentType(request.getEmploymentType())
                .startDate(request.getStartDate())
                .internalNotes(request.getInternalNotes())
                .user(user)
                .build();

        notary = notaryRepository.save(notary);

        String rawToken = UUID.randomUUID() + "-" + UUID.randomUUID();

        UserInvitationToken invitationToken = UserInvitationToken.builder()
                .token(rawToken)
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(1))
                .used(false)
                .build();

        invitationTokenRepository.save(invitationToken);

        String inviteLink = inviteBaseUrl + "/set-password?token=" + rawToken;
        emailService.sendInvitationEmail(user.getEmail(), notary.getFullName(), inviteLink);

        return NotaryInviteResponse.builder()
                .userId(user.getId())
                .notaryId(notary.getId())
                .email(user.getEmail())
                .userStatus(user.getStatus())
                .invitationSent(true)
                .build();
    }
}
