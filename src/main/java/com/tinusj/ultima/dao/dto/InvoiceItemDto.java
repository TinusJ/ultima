package com.tinusj.ultima.dao.dto;

import java.math.BigDecimal;

public record InvoiceItemDto(
        Long id,
        String description,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal total
) {}