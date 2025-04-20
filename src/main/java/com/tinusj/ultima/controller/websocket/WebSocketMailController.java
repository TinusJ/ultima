package com.tinusj.ultima.controller.websocket;

import com.tinusj.ultima.dao.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketMailController {

    @MessageMapping("/mail/{userId}")
    @SendTo("/topic/mail/{userId}")
    public EmailDto notifyNewEmail(@DestinationVariable Long userId, EmailDto emailDto) {
        return emailDto; // Handled by MailService, this is for WebSocket compatibility
    }
}