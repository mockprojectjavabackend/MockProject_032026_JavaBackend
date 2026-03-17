package com.nhom03.mockproject.service;

import org.springframework.stereotype.Service;

import com.nhom03.mockproject.dto.request.blog.ReqCreateBlog;
import com.nhom03.mockproject.dto.response.blog.ResCreateBlog;
import com.nhom03.mockproject.entity.Blog;
import com.nhom03.mockproject.repository.BlogRepository;

/**
 * BlogService
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
 * 17-03-2026 Hthuong04 CreateBlog
 */
@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public ResCreateBlog handleCreateBlog(ReqCreateBlog req) {
        Blog newBlog = new Blog();
        newBlog.setTitle(req.getTitle());
        newBlog.setContent(req.getContent());
        newBlog.setAuthor(req.getAuthor());
        newBlog.setStatus("Pending");

        this.blogRepository.save(newBlog);

        ResCreateBlog res = this.convertToResCreateBlog(newBlog);
        return res;

    }

    public ResCreateBlog convertToResCreateBlog(Blog blog) {
        ResCreateBlog res = new ResCreateBlog();
        res.setId(blog.getId());
        res.setTitle(blog.getTitle());
        res.setContent(blog.getContent());
        res.setAuthor(blog.getAuthor());
        res.setCreatedAt(blog.getCreatedAt());
        res.setStatus(blog.getStatus());
        return res;
    }
}
