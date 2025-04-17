package com.tinusj.ultima.dao.dto;

import java.time.LocalDate;

public record RevenueGraphDataDto(
        LocalDate date,
        double revenue
) {
}