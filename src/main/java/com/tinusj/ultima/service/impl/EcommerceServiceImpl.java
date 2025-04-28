package com.tinusj.ultima.service.impl;

import com.tinusj.ultima.dao.dto.ProductCreateDto;
import com.tinusj.ultima.dao.dto.ProductDto;
import com.tinusj.ultima.dao.dto.ReviewDto;
import com.tinusj.ultima.dao.entity.ProductEntity;
import com.tinusj.ultima.dao.entity.ReviewEntity;
import com.tinusj.ultima.dao.entity.User;
import com.tinusj.ultima.exception.ResourceNotFoundException;
import com.tinusj.ultima.repository.ProductRepository;
import com.tinusj.ultima.repository.ReviewRepository;
import com.tinusj.ultima.repository.UserRepository;
import com.tinusj.ultima.service.EcommerceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EcommerceServiceImpl implements EcommerceService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    private ProductDto mapToDto(ProductEntity product) {
        Double averageRating = product.getReviews().isEmpty() ? null :
                product.getReviews().stream()
                        .mapToInt(ReviewEntity::getRating)
                        .average()
                        .orElse(0.0);

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.getImageUrls(),
                product.getStatus(),
                averageRating,
                product.getReviews().stream()
                        .map(r -> new ReviewDto(
                                r.getId(),
                                r.getRating(),
                                r.getComment(),
                                r.getCreatedAt(),
                                r.getUser().getId(),
                                r.getUser().getEmail()))
                        .collect(Collectors.toList()));
    }

    @Override
    public List<ProductDto> getProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProduct(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        return mapToDto(product);
    }

    @Override
    public ReviewDto submitReview(Long productId, ReviewDto reviewDto) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        ReviewEntity review = new ReviewEntity();
        review.setRating(reviewDto.rating());
        review.setComment(reviewDto.comment());
        review.setCreatedAt(LocalDateTime.now());
        review.setUser(user);
        review.setProduct(product);

        review = reviewRepository.save(review);

        return new ReviewDto(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUser().getId(),
                review.getUser().getEmail());
    }

    @Override
    public List<ProductDto> getRelatedProducts(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
        return productRepository.findAll().stream()
                .filter(p -> !p.getId().equals(productId) && p.getCategory().equals(product.getCategory()))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDto> listProducts(String category, BigDecimal priceMin, BigDecimal priceMax, String keyword, Pageable pageable) {
        return productRepository.findProducts(category, priceMin, priceMax, keyword, pageable)
                .map(this::mapToDto);
    }

    @Override
    public ProductDto createProduct(ProductCreateDto createDto) {
        ProductEntity product = new ProductEntity();
        product.setName(createDto.name());
        product.setDescription(createDto.description());
        product.setPrice(createDto.price());
        product.setStock(createDto.stock());
        product.setCategory(createDto.category());
        product.setImageUrls(createDto.imageUrls());
        product.setStatus(createDto.status());

        product = productRepository.save(product);
        return mapToDto(product);
    }
}