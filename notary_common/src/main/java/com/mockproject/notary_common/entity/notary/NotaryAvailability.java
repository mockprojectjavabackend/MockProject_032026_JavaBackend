package com.mockproject.notary_common.entity.notary;

import com.mockproject.notary_common.constant.FixedDayOffEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalTime;
import java.util.UUID;

/**
 * NotaryAvailability
 *
 * @version 1.0
 * @date 25-03-2026
 * <p>
 * Modification Logs:
 * DATE            AUTHOR       DESCRIPTION
 * -----------------------------------------------
 * 25-03-2026      HuyenThuong  Create
 * 26-03-2026      VanTu        Edit
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notary_availabilities")
public class NotaryAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "working_days_per_week", nullable = false)
    private int workingDaysPerWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "fixed_day_off", length = 16)
    private FixedDayOffEnum fixedDayOff;

    @Column(length = 64)
    private String timezone;

    @OneToOne
    @JoinColumn(name = "notary_id")
    private Notary notary;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (startTime != null) {
            if (endTime == null || !endTime.isAfter(startTime)) {
                throw new IllegalArgumentException("endTime must be after startTime");
            }
        }
        if (startTime == null && endTime != null) {
            throw new IllegalArgumentException("startTime must not be null when endTime is set");
        }
    }
}