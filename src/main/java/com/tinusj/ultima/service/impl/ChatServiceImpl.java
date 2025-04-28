package com.tinusj.ultima.service.impl;

import com.tinusj.ultima.dao.dto.ChatMessageDto;
import com.tinusj.ultima.dao.dto.ConversationDto;
import com.tinusj.ultima.dao.dto.UserSearchDto;
import com.tinusj.ultima.dao.entity.ChatMessageEntity;
import com.tinusj.ultima.dao.entity.ConversationEntity;
import com.tinusj.ultima.dao.entity.User;
import com.tinusj.ultima.exception.ResourceNotFoundException;
import com.tinusj.ultima.repository.ChatMessageRepository;
import com.tinusj.ultima.repository.ConversationRepository;
import com.tinusj.ultima.repository.UserRepository;
import com.tinusj.ultima.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public List<ConversationDto> getConversations() {
        User currentUser = getCurrentUser();
        List<ConversationEntity> conversations = conversationRepository
                .findByParticipant1IdOrParticipant2Id(currentUser.getId(), currentUser.getId());
        return conversations.stream()
                .map(conv -> new ConversationDto(
                        conv.getId(),
                        conv.getParticipant1().getId().equals(currentUser.getId())
                                ? conv.getParticipant2().getId()
                                : conv.getParticipant1().getId(),
                        conv.getParticipant1().getId().equals(currentUser.getId())
                                ? conv.getParticipant2().getEmail()
                                : conv.getParticipant1().getEmail(),
                        conv.getLastMessage(),
                        conv.getLastMessageAt()))
                .collect(Collectors.toList());
    }

    @Override
    public ConversationDto startConversation(Long userId) {
        User currentUser = getCurrentUser();
        User otherUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        if (currentUser.getId().equals(userId)) {
            throw new IllegalArgumentException("Cannot start a conversation with yourself");
        }

        // Check if conversation already exists
        List<ConversationEntity> existing = conversationRepository
                .findByParticipant1IdOrParticipant2Id(currentUser.getId(), currentUser.getId())
                .stream()
                .filter(conv ->
                        (conv.getParticipant1().getId().equals(currentUser.getId()) && conv.getParticipant2().getId().equals(userId)) ||
                                (conv.getParticipant1().getId().equals(userId) && conv.getParticipant2().getId().equals(currentUser.getId())))
                .toList();

        if (!existing.isEmpty()) {
            ConversationEntity conv = existing.get(0);
            return new ConversationDto(
                    conv.getId(),
                    userId,
                    otherUser.getEmail(),
                    conv.getLastMessage(),
                    conv.getLastMessageAt());
        }

        // Create new conversation
        ConversationEntity conversation = new ConversationEntity();
        conversation.setParticipant1(currentUser);
        conversation.setParticipant2(otherUser);
        conversation = conversationRepository.save(conversation);

        return new ConversationDto(
                conversation.getId(),
                userId,
                otherUser.getEmail(),
                null,
                null);
    }

    @Override
    public List<ChatMessageDto> getMessages(Long conversationId) {
        User currentUser = getCurrentUser();
        ConversationEntity conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found: " + conversationId));

        if (!conversation.getParticipant1().getId().equals(currentUser.getId()) &&
                !conversation.getParticipant2().getId().equals(currentUser.getId())) {
            throw new SecurityException("User not authorized to access this conversation");
        }

        return chatMessageRepository.findByConversationId(conversationId).stream()
                .map(msg -> new ChatMessageDto(
                        msg.getId(),
                        msg.getSender().getEmail(),
                        msg.getContent(),
                        msg.getSentAt()))
                .collect(Collectors.toList());
    }

    @Override
    public ChatMessageDto sendMessage(Long conversationId, ChatMessageDto messageDto) {
        User currentUser = getCurrentUser();
        ConversationEntity conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found: " + conversationId));

        if (!conversation.getParticipant1().getId().equals(currentUser.getId()) &&
                !conversation.getParticipant2().getId().equals(currentUser.getId())) {
            throw new SecurityException("User not authorized to access this conversation");
        }

        ChatMessageEntity message = new ChatMessageEntity();
        message.setConversation(conversation);
        message.setSender(currentUser);
        message.setContent(messageDto.content());
        message.setSentAt(LocalDateTime.now());
        message = chatMessageRepository.save(message);

        conversation.setLastMessage(message.getContent());
        conversation.setLastMessageAt(message.getSentAt());
        conversationRepository.save(conversation);

        ChatMessageDto savedMessageDto = new ChatMessageDto(
                message.getId(),
                message.getSender().getEmail(),
                message.getContent(),
                message.getSentAt());

        // Broadcast message via WebSocket
        messagingTemplate.convertAndSend("/topic/conversations/" + conversationId, savedMessageDto);

        return savedMessageDto;
    }

    @Override
    public List<UserSearchDto> searchUsers(String query) {
        User currentUser = getCurrentUser();
        return userRepository.findAll().stream()
                .filter(user -> !user.getId().equals(currentUser.getId()))
                .filter(user -> user.getEmail().toLowerCase().contains(query.toLowerCase()))
                .map(user -> new UserSearchDto(user.getId(), user.getEmail()))
                .collect(Collectors.toList());
    }
}