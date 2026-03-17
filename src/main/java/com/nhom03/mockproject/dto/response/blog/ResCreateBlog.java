package com.nhom03.mockproject.dto.response.blog;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateBlog {
    private Long id;

    private String title;
    private String content;

    private String author;

    private Instant createdAt;

    private String status;
}
