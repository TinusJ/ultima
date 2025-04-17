package com.tinusj.ultima.dao.dto;

import java.time.LocalDate;

public record OrderGraphDataDto(
        LocalDate date,
        long orderCount
) {}