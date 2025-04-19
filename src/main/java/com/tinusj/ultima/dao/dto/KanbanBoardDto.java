package com.tinusj.ultima.dao.dto;

import java.util.List;

public record KanbanBoardDto(
        Long id,
        String title,
        Long ownerId,
        List<KanbanListDto> lists
) {}