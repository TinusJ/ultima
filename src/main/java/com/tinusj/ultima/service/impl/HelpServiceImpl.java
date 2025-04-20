package com.tinusj.ultima.service.impl;

import com.tinusj.ultima.dao.dto.FaqDto;
import com.tinusj.ultima.dao.entity.FaqEntity;
import com.tinusj.ultima.repository.FaqRepository;
import com.tinusj.ultima.service.HelpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HelpServiceImpl implements HelpService {

    private final FaqRepository faqRepository;

    private FaqDto mapToDto(FaqEntity faq) {
        return new FaqDto(
                faq.getId(),
                faq.getQuestion(),
                faq.getAnswer(),
                "support@ultima.com",
                "555-0123",
                "/api/v1/contacts/submit"
        );
    }

    @Override
    public List<FaqDto> getFaqs() {
        return faqRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FaqDto> searchFaqs(String keyword) {
        return faqRepository.findByKeyword(keyword).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}