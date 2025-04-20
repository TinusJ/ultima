package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.FaqDto;

import java.util.List;

public interface HelpService {
    List<FaqDto> getFaqs();
    List<FaqDto> searchFaqs(String keyword);
}