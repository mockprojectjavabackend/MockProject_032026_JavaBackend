package com.nhom03.mockproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * CreateProfileRequest
 *
 * Data Transfer Object for Profile.
 * Used to transfer data between client and server.
 *
 * Version 1.0
 *
 * Date: 17-03-2026
 *
 * Modification Logs:
 * DATE         AUTHOR      DESCRIPTION
 * ----------------------------------------------------------
 * 17-03-2026   Quoc        Create
 */
@Data
@AllArgsConstructor
public class CreateProfileRequest {

    private Long id;
    private String name;
    private String email;
    private String phone;
}
