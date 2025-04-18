package com.tinusj.ultima.dao.dto;

import java.time.LocalDateTime;

public record ConversationDto(
        Long id,
        Long otherParticipantId,
        String otherParticipantUsername,
        String lastMessage,
        LocalDateTime lastMessageAt
) {
}