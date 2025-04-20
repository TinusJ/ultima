package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.ContactDto;
import com.tinusj.ultima.dao.dto.ContactFormDto;

import java.util.List;

public interface ContactService {
    List<ContactDto> getContacts();
    ContactDto submitContactForm(ContactFormDto formDto);
}