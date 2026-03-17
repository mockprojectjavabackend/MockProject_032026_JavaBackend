package com.nhom03.mockproject.dto.request.blog;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCreateBlog {
    private String title;
    private String content;
    private String author;
}
