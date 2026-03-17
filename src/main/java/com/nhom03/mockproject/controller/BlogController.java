package com.nhom03.mockproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhom03.mockproject.dto.request.blog.ReqCreateBlog;
import com.nhom03.mockproject.dto.response.blog.ResCreateBlog;
import com.nhom03.mockproject.service.BlogService;

/**
 * BlogController
 *
 * Version 1.0
 *
 * Date: 17-03-2026
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 17-03-2026 Hthuong04 CreateBlog API
 */
@RestController
@RequestMapping("/api")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping("/blogs")
    public ResponseEntity<ResCreateBlog> createBlog(@RequestBody ReqCreateBlog req) {
        // Implementation for creating a blog
        ResCreateBlog newBlog = this.blogService.handleCreateBlog(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBlog);
    }
}
