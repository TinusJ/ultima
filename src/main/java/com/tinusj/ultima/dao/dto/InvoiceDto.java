package com.tinusj.ultima.dao.dto;


import com.tinusj.ultima.dao.enums.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record InvoiceDto(
        Long id,
        String invoiceNumber,
        LocalDate issueDate,
        LocalDate dueDate,
        InvoiceStatus status,
        Long customerId,
        String customerName,
        String customerAddress,
        String customerEmail,
        String billerName,
        String billerAddress,
        String billerEmail,
        String billerPhone,
        String notes,
        BigDecimal subtotal,
        BigDecimal tax,
        BigDecimal total,
        List<InvoiceItemDto> items
) {}