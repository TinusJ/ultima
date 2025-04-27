package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.TaskCreateDto;
import com.tinusj.ultima.dao.dto.TaskDto;
import com.tinusj.ultima.dao.dto.TaskResponseDto;
import com.tinusj.ultima.dao.dto.TaskUpdateDto;
import com.tinusj.ultima.service.DashboardService;
import com.tinusj.ultima.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "Tasks", description = "API for managing tasks in the Task List application")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {
    private final DashboardService dashboardService;
    private final TaskService taskService;

    @PostMapping
    @Operation(
            summary = "Create a new task",
            description = "Creates a task with the provided details. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task created successfully",
                    content = @Content(schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    public ResponseEntity<TaskResponseDto> createTask(
            @Valid @RequestBody TaskCreateDto taskCreateDTO,
            Authentication authentication) {
        String username = authentication.getName(); // Gets username from JWT
        TaskResponseDto task = taskService.createTask(taskCreateDTO, username);
        return ResponseEntity.ok(task);
    }

    @Operation(
            summary = "Get a task by ID",
            description = "Retrieves a task by its unique ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task found",
                    content = @Content(schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(
            @Parameter(description = "ID of the task to retrieve") @PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @Operation(
            summary = "Get all tasks",
            description = "Retrieves a paginated list of all tasks. Supports pagination and sorting."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of tasks",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<TaskResponseDto>> getAllTasks(
            @Parameter(description = "Pagination and sorting parameters (e.g., page=0&size=10&sort=title,asc)")
            Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllTasks(pageable));
    }

    @Operation(
            summary = "Update a task",
            description = "Updates an existing task by its ID with the provided details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated successfully",
                    content = @Content(schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @Parameter(description = "ID of the task to update") @PathVariable Long id,
            @Valid @RequestBody TaskUpdateDto taskUpdateDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, taskUpdateDTO));
    }

    @Operation(
            summary = "Delete a task",
            description = "Deletes a task by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task to delete") @PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get tasks by status",
            description = "Retrieves a paginated list of tasks filtered by status (e.g., TODO, IN_PROGRESS, DONE)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of tasks",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TaskResponseDto>> getTasksByStatus(
            @Parameter(description = "Status to filter by (TODO, IN_PROGRESS, DONE)") @PathVariable String status,
            Pageable pageable) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status, pageable));
    }

    @Operation(
            summary = "Get tasks by priority",
            description = "Retrieves a paginated list of tasks filtered by priority (e.g., LOW, MEDIUM, HIGH)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of tasks",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/priority/{priority}")
    public ResponseEntity<Page<TaskResponseDto>> getTasksByPriority(
            @Parameter(description = "Priority to filter by (LOW, MEDIUM, HIGH)") @PathVariable String priority,
            Pageable pageable) {
        return ResponseEntity.ok(taskService.getTasksByPriority(priority, pageable));
    }

    @Operation(
            summary = "Get tasks by assignee",
            description = "Retrieves a paginated list of tasks assigned to a specific user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of tasks",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<Page<TaskResponseDto>> getTasksByAssignee(
            @Parameter(description = "ID of the assignee") @PathVariable Long assigneeId,
            Pageable pageable) {
        return ResponseEntity.ok(taskService.getTasksByAssignee(assigneeId, pageable));
    }

    @Operation(
            summary = "Get tasks by creator",
            description = "Retrieves a list of tasks created by the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of tasks",
                    content = @Content(schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/creator")
    public ResponseEntity<List<TaskResponseDto>> getTasksByCreator(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(taskService.getTasksByCreator(username));
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getTasks() {
        return ResponseEntity.ok(dashboardService.getTasks());
    }
}