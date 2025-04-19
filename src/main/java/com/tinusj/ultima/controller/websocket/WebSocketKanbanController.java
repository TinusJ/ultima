package com.tinusj.ultima.controller.websocket;

import com.tinusj.ultima.dao.dto.KanbanTaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketKanbanController {

    @MessageMapping("/boards/{boardId}/tasks")
    @SendTo("/topic/boards/{boardId}")
    public KanbanTaskDto updateTask(@DestinationVariable Long boardId, KanbanTaskDto taskDto) {
        return taskDto; // Handled by KanbanService via REST, this is for WebSocket compatibility
    }
}