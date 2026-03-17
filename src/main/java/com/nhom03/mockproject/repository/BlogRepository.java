package com.nhom03.mockproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhom03.mockproject.entity.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

}
