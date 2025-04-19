package com.tinusj.ultima.controller.pub;

import com.tinusj.ultima.dao.dto.BlogPostDto;
import com.tinusj.ultima.dao.dto.CategoryDto;
import com.tinusj.ultima.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blog")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BlogPostController {

    private final BlogService blogService;

    @Operation(summary = "List published blog posts", description = "Returns a paginated list of published blog posts, with optional filtering by category and keyword search.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @GetMapping("/posts")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<BlogPostDto>> listPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(blogService.listPosts(categoryId, search, pageable));
    }

    @Operation(summary = "Get blog post details", description = "Returns the details of a single published blog post.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found or not published"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @GetMapping("/posts/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BlogPostDto> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.getPost(id));
    }

    @Operation(summary = "Create a new blog post", description = "Creates a new blog post. Restricted to ADMIN or MANAGER roles.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User lacks required role")
    })
    @PostMapping("/posts")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<BlogPostDto> createPost(@RequestBody BlogPostDto postDto) {
        return ResponseEntity.ok(blogService.createPost(postDto));
    }

    @Operation(summary = "Update a blog post", description = "Updates an existing blog post. Restricted to ADMIN or MANAGER roles.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User lacks required role")
    })
    @PutMapping("/posts/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<BlogPostDto> updatePost(
            @PathVariable Long id,
            @RequestBody BlogPostDto postDto) {
        return ResponseEntity.ok(blogService.updatePost(id, postDto));
    }

    @Operation(summary = "List blog categories", description = "Returns a list of all blog categories.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @GetMapping("/categories")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CategoryDto>> listCategories() {
        return ResponseEntity.ok(blogService.listCategories());
    }
}