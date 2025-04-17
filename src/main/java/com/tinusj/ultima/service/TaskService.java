package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.TaskCreateDTO;
import com.tinusj.ultima.dao.dto.TaskResponseDTO;
import com.tinusj.ultima.dao.dto.TaskUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    TaskResponseDTO createTask(TaskCreateDTO taskCreateDTO, String username);

    TaskResponseDTO getTaskById(Long id);

    Page<TaskResponseDTO> getAllTasks(Pageable pageable);

    TaskResponseDTO updateTask(Long id, TaskUpdateDTO taskUpdateDTO);

    void deleteTask(Long id);

    Page<TaskResponseDTO> getTasksByStatus(String status, Pageable pageable);

    Page<TaskResponseDTO> getTasksByPriority(String priority, Pageable pageable);

    Page<TaskResponseDTO> getTasksByAssignee(Long assigneeId, Pageable pageable);

    List<TaskResponseDTO> getTasksByCreator(String username);
}
