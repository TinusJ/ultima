package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.ProductCreateDto;
import com.tinusj.ultima.dao.dto.ProductDto;
import com.tinusj.ultima.dao.dto.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface EcommerceService {
    List<ProductDto> getProducts();
    ProductDto getProduct(Long id);
    ReviewDto submitReview(Long productId, ReviewDto reviewDto);
    List<ProductDto> getRelatedProducts(Long productId);
    Page<ProductDto> listProducts(String category, BigDecimal priceMin, BigDecimal priceMax, String keyword, Pageable pageable);
    ProductDto createProduct(ProductCreateDto createDto);
}