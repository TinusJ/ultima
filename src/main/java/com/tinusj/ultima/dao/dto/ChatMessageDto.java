package com.tinusj.ultima.dao.dto;

import java.time.LocalDateTime;

public record ChatMessageDto(
        Long id,
        String sender,
        String content,
        LocalDateTime sentAt
) {
}
