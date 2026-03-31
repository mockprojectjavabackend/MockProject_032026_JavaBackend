package com.mockproject.notary_admin_server.configuration;

import com.mockproject.notary_admin_server.repository.RoleRepository;
import com.mockproject.notary_admin_server.repository.UserRepository;
import com.mockproject.notary_common.constant.PredefinedRole;
import com.mockproject.notary_common.constant.UserStatus;
import com.mockproject.notary_common.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import com.mockproject.notary_common.entity.Role;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ApplicationInitConfig {
    final PasswordEncoder passwordEncoder;

    @Value("${app.seed.admin.password}")
    String adminPassword;

    @Value("${app.seed.admin.email}")
    String adminEmail;

    @Value("${app.seed.admin.full-name}")
    String adminFullName;

    @Value("${app.seed.admin.phone-number}")
    String adminPhoneNumber;

    @Value("${app.seed.admin.address}")
    String adminAddress;

    @Value("${app.seed.admin.dob}")
    String adminDob;

    @Bean
    @ConditionalOnProperty(prefix = "app.seed", name = "enabled", havingValue = "true")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            log.info("Seeding initial data...");

            if(!StringUtils.hasText(adminPassword)
                || !StringUtils.hasText(adminEmail)) {
                log.warn("Seed admin email or admin password is missing.");
                return;
            }

            Role dispatcherRole = getOrCreateRole(roleRepository, PredefinedRole.DISPATCHER);
            Role notaryRole = getOrCreateRole(roleRepository, PredefinedRole.NOTARY);
            Role adminRole = getOrCreateRole(roleRepository, PredefinedRole.ADMIN);

            boolean adminExists = userRepository.findByEmail(adminEmail).isPresent();

            if (adminExists) {
                log.info("Admin user already exists. Skipping admin seeding.");
                return;
            }

            Set<Role> roles = new HashSet<>();
            roles.add(dispatcherRole);
            roles.add(notaryRole);
            roles.add(adminRole);

            User admin = User.builder()
                    .email(adminEmail)
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .phoneNumber(adminPhoneNumber)
                    .fullName(adminFullName)
                    .dob(LocalDate.parse(adminDob))
                    .address(adminAddress)
                    .status(UserStatus.ACTIVE)
                    .roles(roles)
                    .build();

            userRepository.save(admin);

            log.warn("[INIT] Admin account created. Please change the password immediately.");
            log.info("[INIT] Seeding completed.");
        };
    }

    private Role getOrCreateRole(RoleRepository roleRepository, PredefinedRole predefinedRole) {
        return roleRepository.findByRoleName(predefinedRole)
                .orElseGet(() -> {
                    Role newRole = Role.builder()
                            .roleName(predefinedRole)
                            .build();
                    return roleRepository.save(newRole);
                });
    }
}
