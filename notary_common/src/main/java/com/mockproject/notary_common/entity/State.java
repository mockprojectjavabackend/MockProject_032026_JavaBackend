package com.mockproject.notary_common.entity;

import com.mockproject.notary_common.entity.notary.NotaryServiceArea;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.*;

/**
 * State
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR           DESCRIPTION
 * -----------------------------------------------
 * 25-03-2026      HuyenThuong      create
 * 26-03-2026      VanTu            edit
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "states")
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "state_code", nullable = false, length = 2)
    private String stateCode;

    @Column(name = "state_name", nullable = false, length = 64)
    private String stateName;

    @OneToMany(mappedBy = "state")
    @Builder.Default
    private Set<NotaryServiceArea> notaryServiceAreas = new HashSet<>();

    @OneToMany(mappedBy = "state")
    @Builder.Default
    private Set<StateHoliday> stateHolidays = new HashSet<>();
}
