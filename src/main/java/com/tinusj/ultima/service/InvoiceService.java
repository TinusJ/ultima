package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.InvoiceDto;

public interface InvoiceService {
    InvoiceDto getInvoice(Long id);
}