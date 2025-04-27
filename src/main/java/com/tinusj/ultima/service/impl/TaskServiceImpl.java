package com.tinusj.ultima.service.impl;

import com.tinusj.ultima.dao.dto.TaskCreateDto;
import com.tinusj.ultima.dao.dto.TaskResponseDto;
import com.tinusj.ultima.dao.dto.TaskUpdateDto;
import com.tinusj.ultima.dao.entity.TaskEntity;
import com.tinusj.ultima.dao.entity.User;
import com.tinusj.ultima.dao.enums.TaskPriority;
import com.tinusj.ultima.dao.enums.TaskStatus;
import com.tinusj.ultima.exception.ResourceNotFoundException;
import com.tinusj.ultima.repository.TaskRepository;
import com.tinusj.ultima.repository.UserRepository;
import com.tinusj.ultima.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TaskResponseDto createTask(TaskCreateDto taskCreateDTO, String username) {
        User creator = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(taskCreateDTO.title());
        taskEntity.setDescription(taskCreateDTO.description());
        taskEntity.setStatus(TaskStatus.valueOf(taskCreateDTO.status()));
        taskEntity.setPriority(TaskPriority.valueOf(taskCreateDTO.priority()));
        taskEntity.setDueDate(taskCreateDTO.dueDate());
        taskEntity.setCreator(creator);

        if (taskCreateDTO.assigneeId() != null) {
            User assignee = userRepository.findById(taskCreateDTO.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found: " + taskCreateDTO.assigneeId()));
            taskEntity.setAssignee(assignee);
        }

        taskEntity = taskRepository.save(taskEntity);
        return mapToResponseDTO(taskEntity);
    }

    @Override
    public TaskResponseDto getTaskById(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
        return mapToResponseDTO(taskEntity);
    }

    @Override
    public Page<TaskResponseDto> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(Long id, TaskUpdateDto taskUpdateDTO) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));

        if (taskUpdateDTO.title() != null) taskEntity.setTitle(taskUpdateDTO.title());
        if (taskUpdateDTO.description() != null) taskEntity.setDescription(taskUpdateDTO.description());
        if (taskUpdateDTO.status() != null) taskEntity.setStatus(TaskStatus.valueOf(taskUpdateDTO.status()));
        if (taskUpdateDTO.priority() != null) taskEntity.setPriority(TaskPriority.valueOf(taskUpdateDTO.priority()));
        if (taskUpdateDTO.dueDate() != null) taskEntity.setDueDate(taskUpdateDTO.dueDate());

        if (taskUpdateDTO.assigneeId() != null) {
            User assignee = userRepository.findById(taskUpdateDTO.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found: " + taskUpdateDTO.assigneeId()));
            taskEntity.setAssignee(assignee);
        } else {
            taskEntity.setAssignee(null);
        }

        taskEntity = taskRepository.save(taskEntity);
        return mapToResponseDTO(taskEntity);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found: " + id);
        }
        taskRepository.deleteById(id);
    }

    @Override
    public Page<TaskResponseDto> getTasksByStatus(String status, Pageable pageable) {
        return taskRepository.findByStatus(status, pageable).map(this::mapToResponseDTO);
    }

    @Override
    public Page<TaskResponseDto> getTasksByPriority(String priority, Pageable pageable) {
        return taskRepository.findByPriority(priority, pageable).map(this::mapToResponseDTO);
    }

    @Override
    public Page<TaskResponseDto> getTasksByAssignee(Long assigneeId, Pageable pageable) {
        return taskRepository.findByAssigneeId(assigneeId, pageable).map(this::mapToResponseDTO);
    }

    @Override
    public List<TaskResponseDto> getTasksByCreator(String username) {
        User creator = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return taskRepository.findByCreatorId(creator.getId()).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private TaskResponseDto mapToResponseDTO(TaskEntity taskEntity) {
        return new TaskResponseDto(
                taskEntity.getId(),
                taskEntity.getTitle(),
                taskEntity.getDescription(),
                taskEntity.getStatus().name(),
                taskEntity.getPriority().name(),
                taskEntity.getDueDate(),
                taskEntity.getCreatedAt(),
                taskEntity.getUpdatedAt(),
                taskEntity.getAssignee() != null ? taskEntity.getAssignee().getId() : null,
                taskEntity.getAssignee() != null ? taskEntity.getAssignee().getEmail() : null,
                taskEntity.getCreator().getId(),
                taskEntity.getCreator().getEmail()
        );
    }
}