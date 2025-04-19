package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.KanbanBoardDto;
import com.tinusj.ultima.dao.dto.KanbanListDto;
import com.tinusj.ultima.dao.dto.KanbanTaskDto;
import com.tinusj.ultima.service.KanbanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Boards retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @GetMapping("/boards")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<KanbanBoardDto>> getBoards() {
        return ResponseEntity.ok(kanbanService.getBoards());
    }

    @Operation(summary = "Create a new Kanban board", description = "Creates a new Kanban board with default lists (To Do, In Progress, Done).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Board created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @PostMapping("/boards")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<KanbanBoardDto> createBoard(@RequestBody KanbanBoardDto boardDto) {
        return ResponseEntity.ok(kanbanService.createBoard(boardDto));
    }

    @Operation(summary = "Get Kanban board details", description = "Returns details of a Kanban board, including lists and tasks.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Board retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Board not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @GetMapping("/boards/{boardId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<KanbanBoardDto> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(kanbanService.getBoard(boardId));
    }

    @Operation(summary = "Create a new Kanban list", description = "Creates a new list in the specified Kanban board.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List created successfully"),
            @ApiResponse(responseCode = "404", description = "Board not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @PostMapping("/boards/{boardId}/lists")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<KanbanListDto> createList(
            @PathVariable Long boardId,
            @RequestBody KanbanListDto listDto) {
        return ResponseEntity.ok(kanbanService.createList(boardId, listDto));
    }

    @Operation(summary = "Create a new Kanban task", description = "Creates a new task in the specified Kanban list.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task created successfully"),
            @ApiResponse(responseCode = "404", description = "Board or list not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @PostMapping("/boards/{boardId}/tasks")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<KanbanTaskDto> createTask(
            @PathVariable Long boardId,
            @RequestBody KanbanTaskDto taskDto) {
        return ResponseEntity.ok(kanbanService.createTask(boardId, taskDto));
    }

    @Operation(summary = "Update a Kanban task", description = "Updates the details of a task (title, description, assignees).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task or board not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @PutMapping("/boards/{boardId}/tasks/{taskId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<KanbanTaskDto> updateTask(
            @PathVariable Long boardId,
            @PathVariable Long taskId,
            @RequestBody KanbanTaskDto taskDto) {
        return ResponseEntity.ok(kanbanService.updateTask(boardId, taskId, taskDto));
    }

    @Operation(summary = "Delete a Kanban task", description = "Deletes a task from the specified Kanban board.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task or board not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task moved successfully"),
            @ApiResponse(responseCode = "404", description = "Task, list, or board not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @PutMapping("/boards/{boardId}/tasks/{taskId}/move")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<KanbanTaskDto> moveTask(
            @PathVariable Long boardId,
            @PathVariable Long taskId,
            @RequestParam Long newListId) {
        return ResponseEntity.ok(kanbanService.moveTask(boardId, taskId, newListId));
    }
}