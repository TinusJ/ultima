package com.tinusj.ultima.service.impl;

import com.tinusj.ultima.dao.dto.BlogPostDto;
import com.tinusj.ultima.dao.dto.CategoryDto;
import com.tinusj.ultima.dao.entity.BlogPostEntity;
import com.tinusj.ultima.dao.entity.CategoryEntity;
import com.tinusj.ultima.dao.entity.User;
import com.tinusj.ultima.dao.enums.Role;
import com.tinusj.ultima.exception.ResourceNotFoundException;
import com.tinusj.ultima.repository.BlogPostRepository;
import com.tinusj.ultima.repository.CategoryRepository;
import com.tinusj.ultima.repository.UserRepository;
import com.tinusj.ultima.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogPostRepository blogPostRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public Page<BlogPostDto> listPosts(Long categoryId, String keyword, Pageable pageable) {
        return blogPostRepository.findPublishedPosts(categoryId, keyword, pageable)
                .map(post -> new BlogPostDto(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getThumbnailUrl(),
                        post.getTags(),
                        post.getCategory() != null ? post.getCategory().getId() : null,
                        post.getCategory() != null ? post.getCategory().getName() : null,
                        post.getAuthor().getId(),
                        post.getAuthor().getUsername(),
                        post.getCreatedAt(),
                        post.getPublishDate(),
                        post.isPublished(), 0L));
    }

    @Override
    public BlogPostDto getPost(Long id) {
        BlogPostEntity post = blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + id));
        if (!post.isPublished()) {
            throw new ResourceNotFoundException("Post not published: " + id);
        }
        return new BlogPostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getThumbnailUrl(),
                post.getTags(),
                post.getCategory() != null ? post.getCategory().getId() : null,
                post.getCategory() != null ? post.getCategory().getName() : null,
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getCreatedAt(),
                post.getPublishDate(),
                post.isPublished(), 0L);
    }

    @Override
    public BlogPostDto createPost(BlogPostDto postDto) {
        User currentUser = getCurrentUser();
        CategoryEntity category = postDto.categoryId() != null
                ? categoryRepository.findById(postDto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + postDto.categoryId()))
                : null;

        BlogPostEntity post = new BlogPostEntity();
        post.setTitle(postDto.title());
        post.setContent(postDto.content());
        post.setThumbnailUrl(postDto.thumbnailUrl());
        post.setTags(postDto.tags());
        post.setCategory(category);
        post.setAuthor(currentUser);
        post.setCreatedAt(LocalDateTime.now());
        post.setPublishDate(postDto.isPublished() ? LocalDateTime.now() : null);
        post.setPublished(postDto.isPublished());

        post = blogPostRepository.save(post);

        return new BlogPostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getThumbnailUrl(),
                post.getTags(),
                post.getCategory() != null ? post.getCategory().getId() : null,
                post.getCategory() != null ? post.getCategory().getName() : null,
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getCreatedAt(),
                post.getPublishDate(),
                post.isPublished(), 0L);
    }

    @Override
    public BlogPostDto updatePost(Long id, BlogPostDto postDto) {
        BlogPostEntity post = blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + id));
        User currentUser = getCurrentUser();

        if (!post.getAuthor().getId().equals(currentUser.getId()) && !currentUser.getRoles().contains(Role.ADMIN)) {
            throw new SecurityException("User not authorized to edit this post");
        }

        CategoryEntity category = postDto.categoryId() != null
                ? categoryRepository.findById(postDto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + postDto.categoryId()))
                : null;

        post.setTitle(postDto.title());
        post.setContent(postDto.content());
        post.setThumbnailUrl(postDto.thumbnailUrl());
        post.setTags(postDto.tags());
        post.setCategory(category);
        if (post.isPublished() != postDto.isPublished()) {
            post.setPublished(postDto.isPublished());
            post.setPublishDate(postDto.isPublished() ? LocalDateTime.now() : null);
        }

        post = blogPostRepository.save(post);

        return new BlogPostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getThumbnailUrl(),
                post.getTags(),
                post.getCategory() != null ? post.getCategory().getId() : null,
                post.getCategory() != null ? post.getCategory().getName() : null,
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getCreatedAt(),
                post.getPublishDate(),
                post.isPublished(), 0L);
    }

    @Override
    public List<CategoryDto> listCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDto(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }
}