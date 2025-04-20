package com.tinusj.ultima.dao.dto;

import java.util.Set;

public record ComposeEmailDto(
        Set<Long> recipientToIds,
        Set<Long> recipientCcIds,
        Set<Long> recipientBccIds,
        String subject,
        String body,
        boolean isDraft
) {}