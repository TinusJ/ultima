package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.KanbanBoardDto;
import com.tinusj.ultima.dao.dto.KanbanListDto;
import com.tinusj.ultima.dao.dto.KanbanTaskDto;

import java.util.List;

public interface KanbanService {
    List<KanbanBoardDto> getBoards();

    KanbanBoardDto createBoard(KanbanBoardDto boardDto);

    KanbanBoardDto getBoard(Long boardId);

    KanbanListDto createList(Long boardId, KanbanListDto listDto);

    KanbanTaskDto createTask(Long boardId, KanbanTaskDto taskDto);

    KanbanTaskDto updateTask(Long boardId, Long taskId, KanbanTaskDto taskDto);

    void deleteTask(Long boardId, Long taskId);

    KanbanTaskDto moveTask(Long boardId, Long taskId, Long newListId);
}