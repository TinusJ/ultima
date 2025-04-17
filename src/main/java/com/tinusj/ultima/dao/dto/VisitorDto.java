package com.tinusj.ultima.dao.dto;

import java.time.LocalDate;

public record VisitorDto(
        Long id,
        String source,
        Long visitCount,
        LocalDate visitDate
) {}