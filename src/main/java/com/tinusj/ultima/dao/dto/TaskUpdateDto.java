package com.tinusj.ultima.dao.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TaskUpdateDto(
        @Size(max = 100, message = "Title must be less than 100 characters")
        String title,
        @Size(max = 500, message = "Description must be less than 500 characters")
        String description,
        String status,
        String priority,
        LocalDate dueDate,
        Long assigneeId
) {
}
