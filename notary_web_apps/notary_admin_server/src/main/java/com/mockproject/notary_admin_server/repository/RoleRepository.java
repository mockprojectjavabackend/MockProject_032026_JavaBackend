package com.mockproject.notary_admin_server.repository;

import com.mockproject.notary_common.constant.PredefinedRole;
import com.mockproject.notary_common.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(PredefinedRole roleName);
}
