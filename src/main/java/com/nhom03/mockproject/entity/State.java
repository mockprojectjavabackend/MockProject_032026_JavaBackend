package com.nhom03.mockproject.entity;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "states", uniqueConstraints = {
        @UniqueConstraint(name = "uq_states_code", columnNames = "state_code")
})
@Getter
@Setter
public class State {
    @Id
    @UuidGenerator
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @NotBlank
    @Size(min = 2, max = 2)
    @Column(name = "state_code", nullable = false, length = 2)
    private String stateCode;

    @NotBlank
    @Size(max = 100)
    @Column(name = "state_name", nullable = false, length = 100)
    private String stateName;

    @OneToMany(mappedBy = "state", fetch = FetchType.LAZY)
    private List<NotaryServiceArea> notaryServiceAreas;

}
