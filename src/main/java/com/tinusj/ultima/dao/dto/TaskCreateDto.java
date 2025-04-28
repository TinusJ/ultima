package com.tinusj.ultima.dao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TaskCreateDto(
        @NotBlank(message = "Title is mandatory") @Size(max = 100, message = "Title must be less than 100 characters") String title,
        @Size(max = 500, message = "Description must be less than 500 characters") String description,
        @NotBlank(message = "Status is mandatory") String status,
        @NotBlank(message = "Priority is mandatory") String priority, LocalDate dueDate, Long assigneeId) {
}
