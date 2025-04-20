package com.tinusj.ultima.dao.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record EmailDto(
        Long id,
        Long senderId,
        String senderUsername,
        Set<Long> recipientToIds,
        Set<Long> recipientCcIds,
        Set<Long> recipientBccIds,
        String subject,
        String body,
        LocalDateTime sentAt,
        boolean isRead,
        Long folderId,
        String folderName
) {}