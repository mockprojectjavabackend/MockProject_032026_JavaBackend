package com.nhom03.mockproject.sample.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TaskRequestDTO
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
 * 17-03-2026         Tho Ha             CreateTask DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {

    private String id;

    private String taskName;

}
