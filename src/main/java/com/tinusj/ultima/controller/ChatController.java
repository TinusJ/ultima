package com.tinusj.ultima.controller;

import com.tinusj.ultima.dao.dto.ApiResponse;
import com.tinusj.ultima.dao.dto.ChatMessageDto;
import com.tinusj.ultima.dao.dto.ConversationDto;
import com.tinusj.ultima.dao.dto.UserSearchDto;
import com.tinusj.ultima.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "Get authenticated user's conversations", description = "Returns a list of conversations for the authenticated user, including the other participant and last message.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of conversations retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @GetMapping("/conversations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ConversationDto>>> getConversations() {
        return ResponseEntity.ok(ApiResponse.ok(chatService.getConversations()));
    }

    @Operation(summary = "Start a new conversation", description = "Creates a new conversation with the specified user.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Conversation created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user ID"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @PostMapping("/conversations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ConversationDto>> startConversation(@RequestParam Long userId) {
        return ResponseEntity.ok(ApiResponse.ok(chatService.startConversation(userId)));
    }

    @Operation(summary = "Get messages for a conversation", description = "Returns all messages in the specified conversation.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Messages retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Conversation not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @GetMapping("/conversations/{conversationId}/messages")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ChatMessageDto>>> getMessages(@PathVariable Long conversationId) {
        return ResponseEntity.ok(ApiResponse.ok(chatService.getMessages(conversationId)));
    }

    @Operation(summary = "Send a message in a conversation", description = "Sends a message in the specified conversation and broadcasts it via WebSocket.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Message sent successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Conversation not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @PostMapping("/conversations/{conversationId}/messages")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ChatMessageDto>> sendMessage(
            @PathVariable Long conversationId,
            @RequestBody ChatMessageDto messageDto) {
        return ResponseEntity.ok(ApiResponse.ok(chatService.sendMessage(conversationId, messageDto)));
    }

    @Operation(summary = "Search users by username", description = "Returns a list of users matching the search query for starting a conversation.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @GetMapping("/users/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<UserSearchDto>>> searchUsers(@RequestParam String query) {
        return ResponseEntity.ok(ApiResponse.ok(chatService.searchUsers(query)));
    }
}