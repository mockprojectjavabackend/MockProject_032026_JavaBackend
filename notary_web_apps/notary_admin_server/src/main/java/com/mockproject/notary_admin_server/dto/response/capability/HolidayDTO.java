package com.mockproject.notary_admin_server.dto.response.capability;

import lombok.*;

import java.time.LocalDate;

/**
 * HolidayDTO
 *
 * @version 1.0

 * Modification Logs:
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------
 * 27-03-2026      ThoHa       create
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HolidayDTO {
    private String name;
    private LocalDate date;
    private String type;
    private String description;

}
