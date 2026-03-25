package com.nhom03.mockproject.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.nhom03.mockproject.constant.FixedDayOffEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notary_availabilities", uniqueConstraints = {
        @UniqueConstraint(name = "uq_notary_availability", columnNames = "notary_id")
})
@Getter
@Setter
public class NotaryAvailability {
    @Id
    @UuidGenerator
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notary_id", nullable = false, unique = true)
    private Notary notary;

    @Min(1)
    @Max(7)
    @NotNull
    @Column(name = "working_days_per_week", nullable = false)
    private Integer workingDaysPerWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "fixed_day_off", length = 100)
    private FixedDayOffEnum fixedDayOff;

    @Size(max = 50)
    @Column(name = "timezone", length = 50)
    private String timezone;

    @PrePersist
    @PreUpdate
    public void validate() {
        // validate time range
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
