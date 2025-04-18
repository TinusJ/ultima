package com.tinusj.ultima.controller.websocket;

import com.tinusj.ultima.dao.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketChatController {

    @MessageMapping("/conversations/{conversationId}/send")
    @SendTo("/topic/conversations/{conversationId}")
    public ChatMessageDto sendMessage(@DestinationVariable Long conversationId, ChatMessageDto messageDto) {
        return messageDto; // Handled by ChatService via REST, this is for WebSocket compatibility
    }
}