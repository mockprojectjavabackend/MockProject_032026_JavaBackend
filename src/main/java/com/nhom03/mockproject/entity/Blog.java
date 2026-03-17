package com.nhom03.mockproject.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "blogs")
@Getter
@Setter
public class Blog {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String title;
    private String content;

    private String author;

    private Instant createdAt;

    private Instant updatedAt;

    private String status;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();

    }
}
