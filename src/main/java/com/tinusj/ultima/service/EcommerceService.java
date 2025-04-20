package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.ProductDto;
import com.tinusj.ultima.dao.dto.ReviewDto;

import java.util.List;

public interface EcommerceService {
    List<ProductDto> getProducts();
    ProductDto getProduct(Long id);
    ReviewDto submitReview(Long productId, ReviewDto reviewDto);
    List<ProductDto> getRelatedProducts(Long productId);
}