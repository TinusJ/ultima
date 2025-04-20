package com.tinusj.ultima.controller.pub;

import com.tinusj.ultima.dao.dto.ProductCreateDto;
import com.tinusj.ultima.dao.dto.ProductDto;
import com.tinusj.ultima.dao.dto.ReviewDto;
import com.tinusj.ultima.service.EcommerceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final EcommerceService ecommerceService;

    @Operation(summary = "Get all products for dashboard", description = "Returns a list of all products for the dashboard.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User lacks required role")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<ProductDto>> getProducts() {
        return ResponseEntity.ok(ecommerceService.getProducts());
    }

    @Operation(summary = "Get product details", description = "Returns the details of a single product, including reviews. Public endpoint.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(ecommerceService.getProduct(id));
    }

    @Operation(summary = "Submit a product review", description = "Submits a review for a product. Requires authentication.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Review submitted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User lacks required role"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/{id}/reviews")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<ReviewDto> submitReview(@PathVariable Long id, @RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok(ecommerceService.submitReview(id, reviewDto));
    }

    @Operation(summary = "Get related products", description = "Returns a list of products related to the specified product. Public endpoint.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Related products retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}/related")
    public ResponseEntity<List<ProductDto>> getRelatedProducts(@PathVariable Long id) {
        return ResponseEntity.ok(ecommerceService.getRelatedProducts(id));
    }

    @Operation(summary = "List products with filters and pagination", description = "Returns a paginated list of products with optional filtering by category, price range, and search keyword, and sorting by price or name. Public endpoint.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/list")
    public ResponseEntity<Page<ProductDto>> listProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(ecommerceService.listProducts(category, priceMin, priceMax, keyword, pageable));
    }

    @Operation(summary = "Create a new product", description = "Creates a new product. Requires admin or manager role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User lacks required role"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductCreateDto createDto) {
        return ResponseEntity.ok(ecommerceService.createProduct(createDto));
    }
}