package com.tinusj.ultima.dao.dto;

import java.util.List;

public record KanbanListDto(
        Long id,
        String title,
        Long boardId,
        List<KanbanTaskDto> tasks
) {}