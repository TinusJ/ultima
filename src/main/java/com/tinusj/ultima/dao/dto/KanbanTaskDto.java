package com.tinusj.ultima.dao.dto;

import java.util.Set;

public record KanbanTaskDto(
        Long id,
        String title,
        String description,
        Long kanbanListId,
        Set<Long> assigneeIds
) {}