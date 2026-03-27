package com.mockproject.notary_admin_server.dto.response;

import lombok.*;

import java.time.LocalDate;

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
