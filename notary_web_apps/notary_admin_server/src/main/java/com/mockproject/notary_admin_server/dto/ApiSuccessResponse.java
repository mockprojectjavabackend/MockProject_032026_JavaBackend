package com.mockproject.notary_admin_server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiSuccessResponse<T> {
    int status;
    String message;
    T data;
    String timestamp;

    public static <T> ApiSuccessResponse<T> success(int status, String message, T data) {
        return ApiSuccessResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .timestamp(Instant.now().toString())
                .build();
    }

    public static <T> ApiSuccessResponse<T> ok(T data) {
        return success(200, "Request successful", data);
    }

    public static <T> ApiSuccessResponse<T> created(T data) {
        return success(201, "Created successfully", data);
    }

    public static ApiSuccessResponse<Void> deleted() {
        return ApiSuccessResponse.<Void>builder()
                .status(204)
                .message("Deleted successfully")
                .timestamp(Instant.now().toString())
                .build();
    }
}
