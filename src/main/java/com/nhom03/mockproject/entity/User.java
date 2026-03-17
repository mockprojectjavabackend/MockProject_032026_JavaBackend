package com.nhom03.mockproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {
    private int id;
    private String name;
    private int age;
    private String phone;
    private String email;
}
