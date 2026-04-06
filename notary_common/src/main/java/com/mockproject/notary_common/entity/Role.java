package com.mockproject.notary_common.entity;

import com.mockproject.notary_common.constant.PredefinedRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

/**
 * Role
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 25-03-2026      TranMinh    create
 * 26-03-2026      VanTu       edit
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false, unique = true, length = 16)
    private PredefinedRole roleName;
}
