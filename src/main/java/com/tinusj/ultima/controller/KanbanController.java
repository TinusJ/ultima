package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.ApiResponse;
import com.tinusj.ultima.dao.dto.KanbanBoardDto;
import com.tinusj.ultima.dao.dto.KanbanListDto;
import com.tinusj.ultima.dao.dto.KanbanTaskDto;
import com.tinusj.ultima.service.KanbanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kanban")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class KanbanController {

    private final KanbanService kanbanService;

    @Operation(summary = "Get authenticated user's Kanban boards", description = "Returns a list of Kanban boards owned by the authenticated user.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Boards retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @GetMapping("/boards")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<KanbanBoardDto>>> getBoards() {
        return ResponseEntity.ok(ApiResponse.ok(kanbanService.getBoards()));
    }

    @Operation(summary = "Create a new Kanban board", description = "Creates a new Kanban board with default lists (To Do, In Progress, Done).")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Board created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @PostMapping("/boards")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<KanbanBoardDto>> createBoard(@RequestBody KanbanBoardDto boardDto) {
        return ResponseEntity.ok(ApiResponse.ok(kanbanService.createBoard(boardDto)));
    }

    @Operation(summary = "Get Kanban board details", description = "Returns details of a Kanban board, including lists and tasks.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Board retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Board not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @GetMapping("/boards/{boardId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<KanbanBoardDto>> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(ApiResponse.ok(kanbanService.getBoard(boardId)));
    }

    @Operation(summary = "Create a new Kanban list", description = "Creates a new list in the specified Kanban board.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Board not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @PostMapping("/boards/{boardId}/lists")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<KanbanListDto>> createList(
            @PathVariable Long boardId,
            @RequestBody KanbanListDto listDto) {
        return ResponseEntity.ok(ApiResponse.ok(kanbanService.createList(boardId, listDto)));
    }

    @Operation(summary = "Create a new Kanban task", description = "Creates a new task in the specified Kanban list.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Board or list not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @PostMapping("/boards/{boardId}/tasks")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<KanbanTaskDto>> createTask(
            @PathVariable Long boardId,
            @RequestBody KanbanTaskDto taskDto) {
        return ResponseEntity.ok(ApiResponse.ok(kanbanService.createTask(boardId, taskDto)));
    }

    @Operation(summary = "Update a Kanban task", description = "Updates the details of a task (title, description, assignees).")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task or board not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @PutMapping("/boards/{boardId}/tasks/{taskId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<KanbanTaskDto>> updateTask(
            @PathVariable Long boardId,
            @PathVariable Long taskId,
            @RequestBody KanbanTaskDto taskDto) {
        return ResponseEntity.ok(ApiResponse.ok(kanbanService.updateTask(boardId, taskId, taskDto)));
    }

    @Operation(summary = "Delete a Kanban task", description = "Deletes a task from the specified Kanban board.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task or board not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @DeleteMapping("/boards/{boardId}/tasks/{taskId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long boardId,
            @PathVariable Long taskId) {
        kanbanService.deleteTask(boardId, taskId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Move a Kanban task to a different list", description = "Moves a task to a different Kanban list within the same board.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task moved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task, list, or board not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @PutMapping("/boards/{boardId}/tasks/{taskId}/move")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<KanbanTaskDto>> moveTask(
            @PathVariable Long boardId,
            @PathVariable Long taskId,
            @RequestParam Long newListId) {
        return ResponseEntity.ok(ApiResponse.ok(kanbanService.moveTask(boardId, taskId, newListId)));
    }
}