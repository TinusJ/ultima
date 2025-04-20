package com.tinusj.ultima.service.impl;

import com.tinusj.ultima.dao.dto.InvoiceDto;
import com.tinusj.ultima.dao.dto.InvoiceItemDto;
import com.tinusj.ultima.dao.entity.InvoiceEntity;
import com.tinusj.ultima.exception.ResourceNotFoundException;
import com.tinusj.ultima.repository.InvoiceRepository;
import com.tinusj.ultima.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Override
    public InvoiceDto getInvoice(Long id) {
        InvoiceEntity invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found: " + id));

        return new InvoiceDto(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getIssueDate(),
                invoice.getDueDate(),
                invoice.getStatus(),
                invoice.getCustomer().getId(),
                invoice.getCustomer().getName(),
                invoice.getCustomer().getAddress(),
                invoice.getCustomer().getEmail(),
                invoice.getBillerName(),
                invoice.getBillerAddress(),
                invoice.getBillerEmail(),
                invoice.getBillerPhone(),
                invoice.getNotes(),
                invoice.getSubtotal(),
                invoice.getTax(),
                invoice.getTotal(),
                invoice.getItems().stream()
                        .map(item -> new InvoiceItemDto(
                                item.getId(),
                                item.getDescription(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                item.getTotal()))
                        .collect(Collectors.toList()));
    }
}