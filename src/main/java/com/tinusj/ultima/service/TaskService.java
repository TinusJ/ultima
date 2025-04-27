package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.TaskCreateDto;
import com.tinusj.ultima.dao.dto.TaskResponseDto;
import com.tinusj.ultima.dao.dto.TaskUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(TaskCreateDto taskCreateDTO, String username);

    TaskResponseDto getTaskById(Long id);

    Page<TaskResponseDto> getAllTasks(Pageable pageable);

    TaskResponseDto updateTask(Long id, TaskUpdateDto taskUpdateDTO);

    void deleteTask(Long id);

    Page<TaskResponseDto> getTasksByStatus(String status, Pageable pageable);

    Page<TaskResponseDto> getTasksByPriority(String priority, Pageable pageable);

    Page<TaskResponseDto> getTasksByAssignee(Long assigneeId, Pageable pageable);

    List<TaskResponseDto> getTasksByCreator(String username);
}
