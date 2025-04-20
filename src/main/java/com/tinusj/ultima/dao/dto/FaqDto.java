package com.tinusj.ultima.dao.dto;

public record FaqDto(
        Long id,
        String question,
        String answer,
        String contactEmail,
        String contactPhone,
        String contactFormUrl
) {}