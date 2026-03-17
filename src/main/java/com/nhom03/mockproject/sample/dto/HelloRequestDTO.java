package com.nhom03.mockproject.sample.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HelloRequestDTO
 *
 * Version 1.0
 *
 * Date: 17-03-2026
 *
 * Copyright
 *
 * Modification Logs:
 * DATE                 AUTHOR          DESCRIPTION
 * -----------------------------------------------------------------------
 * 17-03-2026         Agent             Create DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelloRequestDTO {

    private String name;
    
    private String message;

}
