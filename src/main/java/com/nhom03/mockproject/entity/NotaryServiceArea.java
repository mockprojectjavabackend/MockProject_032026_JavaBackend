package com.nhom03.mockproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notary_service_areas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaryServiceArea {

    @Id
    @Column(columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notary_id", nullable = false)
    private Notary notary;

    @Column(name = "state_id", nullable = false)
    private Long stateId;

    @Size(max = 120)
    @Column(name = "county_name", length = 120, nullable = false)
    private String countyName;

    @Size(max = 120)
    @Column(name = "city_name", length = 120)
    private String cityName;

    @Size(max = 20)
    @Column(name = "zip_code", length = 20)
    private String zipCode;

    @Min(0)
    @Column(name = "max_travel_miles")
    private Integer maxTravelMiles;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
