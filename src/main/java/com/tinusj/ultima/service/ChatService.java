package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.ChatMessageDto;
import com.tinusj.ultima.dao.dto.ConversationDto;
import com.tinusj.ultima.dao.dto.UserSearchDto;

import java.util.List;

public interface ChatService {
    List<ConversationDto> getConversations();

    ConversationDto startConversation(Long userId);

    List<ChatMessageDto> getMessages(Long conversationId);

    ChatMessageDto sendMessage(Long conversationId, ChatMessageDto messageDto);

    List<UserSearchDto> searchUsers(String query);
}