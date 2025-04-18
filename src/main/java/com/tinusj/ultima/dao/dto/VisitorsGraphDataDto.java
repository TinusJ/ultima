package com.tinusj.ultima.dao.dto;

import java.time.LocalDate;

public record VisitorsGraphDataDto(
        LocalDate date,
        long visitCount
) {
}