package com.tinusj.ultima.dao.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        String status,
        String priority,
        LocalDate dueDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long assigneeId,
        String assigneeUsername,
        Long creatorId,
        String creatorUsername
) {}