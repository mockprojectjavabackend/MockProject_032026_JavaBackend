package com.mockproject.notary_admin_server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    int status;
    String path;
    Map<String, String> errors;
    String timestamp;

    public static ApiErrorResponse of(int status, String path, Map<String, String> errors) {
        return ApiErrorResponse.builder()
                .status(status)
                .path(path)
                .errors(errors)
                .timestamp(Instant.now().toString())
                .build();
    }
}