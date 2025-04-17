package com.tinusj.ultima.service.impl;

import com.tinusj.ultima.dao.dto.TaskCreateDTO;
import com.tinusj.ultima.dao.dto.TaskResponseDTO;
import com.tinusj.ultima.dao.dto.TaskUpdateDTO;
import com.tinusj.ultima.dao.entity.Task;
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
    public TaskResponseDTO createTask(TaskCreateDTO taskCreateDTO, String username) {
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Task task = new Task();
        task.setTitle(taskCreateDTO.title());
        task.setDescription(taskCreateDTO.description());
        task.setStatus(TaskStatus.valueOf(taskCreateDTO.status()));
        task.setPriority(TaskPriority.valueOf(taskCreateDTO.priority()));
        task.setDueDate(taskCreateDTO.dueDate());
        task.setCreator(creator);

        if (taskCreateDTO.assigneeId() != null) {
            User assignee = userRepository.findById(taskCreateDTO.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found: " + taskCreateDTO.assigneeId()));
            task.setAssignee(assignee);
        }

        task = taskRepository.save(task);
        return mapToResponseDTO(task);
    }

    @Override
    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
        return mapToResponseDTO(task);
    }

    @Override
    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskUpdateDTO taskUpdateDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));

        if (taskUpdateDTO.title() != null) task.setTitle(taskUpdateDTO.title());
        if (taskUpdateDTO.description() != null) task.setDescription(taskUpdateDTO.description());
        if (taskUpdateDTO.status() != null) task.setStatus(TaskStatus.valueOf(taskUpdateDTO.status()));
        if (taskUpdateDTO.priority() != null) task.setPriority(TaskPriority.valueOf(taskUpdateDTO.priority()));
        if (taskUpdateDTO.dueDate() != null) task.setDueDate(taskUpdateDTO.dueDate());

        if (taskUpdateDTO.assigneeId() != null) {
            User assignee = userRepository.findById(taskUpdateDTO.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found: " + taskUpdateDTO.assigneeId()));
            task.setAssignee(assignee);
        } else {
            task.setAssignee(null);
        }

        task = taskRepository.save(task);
        return mapToResponseDTO(task);
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
    public Page<TaskResponseDTO> getTasksByStatus(String status, Pageable pageable) {
        return taskRepository.findByStatus(status, pageable).map(this::mapToResponseDTO);
    }

    @Override
    public Page<TaskResponseDTO> getTasksByPriority(String priority, Pageable pageable) {
        return taskRepository.findByPriority(priority, pageable).map(this::mapToResponseDTO);
    }

    @Override
    public Page<TaskResponseDTO> getTasksByAssignee(Long assigneeId, Pageable pageable) {
        return taskRepository.findByAssigneeId(assigneeId, pageable).map(this::mapToResponseDTO);
    }

    @Override
    public List<TaskResponseDTO> getTasksByCreator(String username) {
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return taskRepository.findByCreatorId(creator.getId()).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private TaskResponseDTO mapToResponseDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getPriority().name(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getAssignee() != null ? task.getAssignee().getId() : null,
                task.getAssignee() != null ? task.getAssignee().getUsername() : null,
                task.getCreator().getId(),
                task.getCreator().getUsername()
        );
    }
}