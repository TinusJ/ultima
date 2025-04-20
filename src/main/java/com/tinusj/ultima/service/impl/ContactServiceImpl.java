package com.tinusj.ultima.service.impl;

import com.tinusj.ultima.dao.dto.ContactDto;
import com.tinusj.ultima.dao.dto.ContactFormDto;
import com.tinusj.ultima.dao.entity.ContactEntity;
import com.tinusj.ultima.repository.ContactRepository;
import com.tinusj.ultima.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Override
    public List<ContactDto> getContacts() {
        return contactRepository.findAll().stream()
                .map(c -> new ContactDto(
                        c.getId(),
                        c.getName(),
                        c.getEmail(),
                        c.getPhone(),
                        c.getMessage()))
                .collect(Collectors.toList());
    }

    @Override
    public ContactDto submitContactForm(ContactFormDto formDto) {
        ContactEntity contact = new ContactEntity();
        contact.setName(formDto.name());
        contact.setEmail(formDto.email());
        contact.setMessage(formDto.message());
        // Phone is optional, not set from form
        contact = contactRepository.save(contact);

        return new ContactDto(
                contact.getId(),
                contact.getName(),
                contact.getEmail(),
                contact.getPhone(),
                contact.getMessage());
    }
}