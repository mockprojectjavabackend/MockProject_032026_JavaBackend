package com.nhom03.mockproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name="id", updatable=false, nullable=false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "role_name", nullable = false, unique = true, length = 20)
    private String roleName;
}
