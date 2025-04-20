package com.tinusj.ultima.dao.dto;

import com.tinusj.ultima.dao.enums.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record ProductCreateDto(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock cannot be negative")
        Integer stock,

        @NotBlank(message = "Category is required")
        String category,

        @NotEmpty(message = "At least one image URL is required")
        List<@NotBlank(message = "Image URL cannot be blank") String> imageUrls,

        @NotNull(message = "Status is required")
        ProductStatus status
) {}