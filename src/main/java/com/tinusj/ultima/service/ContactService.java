package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.ContactDto;
import com.tinusj.ultima.dao.dto.ContactFormDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactService {
    Page<ContactDto> getContacts(Pageable pageable);
    ContactDto submitContactForm(ContactFormDto formDto);
}