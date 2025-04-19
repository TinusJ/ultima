package com.tinusj.ultima.service.impl;

import com.tinusj.ultima.dao.dto.KanbanBoardDto;
import com.tinusj.ultima.dao.dto.KanbanListDto;
import com.tinusj.ultima.dao.dto.KanbanTaskDto;
import com.tinusj.ultima.dao.entity.KanbanBoardEntity;
import com.tinusj.ultima.dao.entity.KanbanListEntity;
import com.tinusj.ultima.dao.entity.KanbanTaskEntity;
import com.tinusj.ultima.dao.entity.User;
import com.tinusj.ultima.exception.ResourceNotFoundException;
import com.tinusj.ultima.repository.KanbanBoardRepository;
import com.tinusj.ultima.repository.KanbanListRepository;
import com.tinusj.ultima.repository.KanbanTaskRepository;
import com.tinusj.ultima.repository.UserRepository;
import com.tinusj.ultima.service.KanbanService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KanbanServiceImpl implements KanbanService {

    private final KanbanBoardRepository boardRepository;
    private final KanbanListRepository listRepository;
    private final KanbanTaskRepository taskRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void checkBoardAccess(KanbanBoardEntity board) {
        User currentUser = getCurrentUser();
        if (!board.getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("User not authorized to access this board");
        }
    }

    @Override
    public List<KanbanBoardDto> getBoards() {
        User currentUser = getCurrentUser();
        return boardRepository.findByOwnerId(currentUser.getId()).stream()
                .map(board -> new KanbanBoardDto(
                        board.getId(),
                        board.getTitle(),
                        board.getOwner().getId(),
                        listRepository.findByKanbanBoardId(board.getId()).stream()
                                .map(list -> new KanbanListDto(
                                        list.getId(),
                                        list.getTitle(),
                                        list.getKanbanBoard().getId(),
                                        taskRepository.findByKanbanListId(list.getId()).stream()
                                                .map(task -> new KanbanTaskDto(
                                                        task.getId(),
                                                        task.getTitle(),
                                                        task.getDescription(),
                                                        task.getKanbanList().getId(),
                                                        task.getAssignees().stream()
                                                                .map(User::getId)
                                                                .collect(Collectors.toSet())))
                                                .collect(Collectors.toList())))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public KanbanBoardDto createBoard(KanbanBoardDto boardDto) {
        User currentUser = getCurrentUser();
        KanbanBoardEntity board = new KanbanBoardEntity();
        board.setTitle(boardDto.title());
        board.setOwner(currentUser);
        board = boardRepository.save(board);

        // Create default lists
        String[] defaultLists = {"To Do", "In Progress", "Done"};
        for (String listTitle : defaultLists) {
            KanbanListEntity list = new KanbanListEntity();
            list.setTitle(listTitle);
            list.setKanbanBoard(board);
            listRepository.save(list);
        }

        KanbanBoardDto savedBoard = new KanbanBoardDto(
                board.getId(),
                board.getTitle(),
                board.getOwner().getId(),
                listRepository.findByKanbanBoardId(board.getId()).stream()
                        .map(list -> new KanbanListDto(
                                list.getId(),
                                list.getTitle(),
                                list.getKanbanBoard().getId(),
                                List.of()))
                        .collect(Collectors.toList()));

        messagingTemplate.convertAndSend("/topic/boards/" + board.getId(), savedBoard);
        return savedBoard;
    }

    @Override
    public KanbanBoardDto getBoard(Long boardId) {
        KanbanBoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found: " + boardId));
        checkBoardAccess(board);

        return new KanbanBoardDto(
                board.getId(),
                board.getTitle(),
                board.getOwner().getId(),
                listRepository.findByKanbanBoardId(board.getId()).stream()
                        .map(list -> new KanbanListDto(
                                list.getId(),
                                list.getTitle(),
                                list.getKanbanBoard().getId(),
                                taskRepository.findByKanbanListId(list.getId()).stream()
                                        .map(task -> new KanbanTaskDto(
                                                task.getId(),
                                                task.getTitle(),
                                                task.getDescription(),
                                                task.getKanbanList().getId(),
                                                task.getAssignees().stream()
                                                        .map(User::getId)
                                                        .collect(Collectors.toSet())))
                                        .collect(Collectors.toList())))
                        .collect(Collectors.toList()));
    }

    @Override
    public KanbanListDto createList(Long boardId, KanbanListDto listDto) {
        KanbanBoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found: " + boardId));
        checkBoardAccess(board);

        KanbanListEntity list = new KanbanListEntity();
        list.setTitle(listDto.title());
        list.setKanbanBoard(board);
        list = listRepository.save(list);

        KanbanListDto savedList = new KanbanListDto(
                list.getId(),
                list.getTitle(),
                list.getKanbanBoard().getId(),
                List.of());

        messagingTemplate.convertAndSend("/topic/boards/" + boardId, getBoard(boardId));
        return savedList;
    }

    @Override
    public KanbanTaskDto createTask(Long boardId, KanbanTaskDto taskDto) {
        KanbanBoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found: " + boardId));
        checkBoardAccess(board);

        KanbanListEntity list = listRepository.findById(taskDto.kanbanListId())
                .orElseThrow(() -> new ResourceNotFoundException("List not found: " + taskDto.kanbanListId()));
        if (!list.getKanbanBoard().getId().equals(boardId)) {
            throw new IllegalArgumentException("List does not belong to the specified board");
        }

        KanbanTaskEntity task = new KanbanTaskEntity();
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setKanbanList(list);

        if (taskDto.assigneeIds() != null) {
            Set<User> assignees = taskDto.assigneeIds().stream()
                    .map(userId -> userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId)))
                    .collect(Collectors.toSet());
            task.setAssignees(assignees);
        }

        task = taskRepository.save(task);

        KanbanTaskDto savedTask = new KanbanTaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getKanbanList().getId(),
                task.getAssignees().stream().map(User::getId).collect(Collectors.toSet()));

        messagingTemplate.convertAndSend("/topic/boards/" + boardId, getBoard(boardId));
        return savedTask;
    }

    @Override
    public KanbanTaskDto updateTask(Long boardId, Long taskId, KanbanTaskDto taskDto) {
        KanbanBoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found: " + boardId));
        checkBoardAccess(board);

        KanbanTaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));
        if (!task.getKanbanList().getKanbanBoard().getId().equals(boardId)) {
            throw new IllegalArgumentException("Task does not belong to the specified board");
        }

        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());

        if (taskDto.assigneeIds() != null) {
            Set<User> assignees = taskDto.assigneeIds().stream()
                    .map(userId -> userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId)))
                    .collect(Collectors.toSet());
            task.setAssignees(assignees);
        } else {
            task.setAssignees(Set.of());
        }

        task = taskRepository.save(task);

        KanbanTaskDto updatedTask = new KanbanTaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getKanbanList().getId(),
                task.getAssignees().stream().map(User::getId).collect(Collectors.toSet()));

        messagingTemplate.convertAndSend("/topic/boards/" + boardId, getBoard(boardId));
        return updatedTask;
    }

    @Override
    public void deleteTask(Long boardId, Long taskId) {
        KanbanBoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found: " + boardId));
        checkBoardAccess(board);

        KanbanTaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));
        if (!task.getKanbanList().getKanbanBoard().getId().equals(boardId)) {
            throw new IllegalArgumentException("Task does not belong to the specified board");
        }

        taskRepository.delete(task);
        messagingTemplate.convertAndSend("/topic/boards/" + boardId, getBoard(boardId));
    }

    @Override
    public KanbanTaskDto moveTask(Long boardId, Long taskId, Long newListId) {
        KanbanBoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found: " + boardId));
        checkBoardAccess(board);

        KanbanTaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));
        if (!task.getKanbanList().getKanbanBoard().getId().equals(boardId)) {
            throw new IllegalArgumentException("Task does not belong to the specified board");
        }

        KanbanListEntity newList = listRepository.findById(newListId)
                .orElseThrow(() -> new ResourceNotFoundException("List not found: " + newListId));
        if (!newList.getKanbanBoard().getId().equals(boardId)) {
            throw new IllegalArgumentException("New list does not belong to the specified board");
        }

        task.setKanbanList(newList);
        task = taskRepository.save(task);

        KanbanTaskDto movedTask = new KanbanTaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getKanbanList().getId(),
                task.getAssignees().stream().map(User::getId).collect(Collectors.toSet()));

        messagingTemplate.convertAndSend("/topic/boards/" + boardId, getBoard(boardId));
        return movedTask;
    }
}