package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.BlogPostDto;
import com.tinusj.ultima.dao.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogService {
    Page<BlogPostDto> listPosts(Long categoryId, String keyword, Pageable pageable);
    BlogPostDto getPost(Long id);
    BlogPostDto createPost(BlogPostDto postDto);
    BlogPostDto updatePost(Long id, BlogPostDto postDto);
    List<CategoryDto> listCategories();
}