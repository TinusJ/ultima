package com.tinusj.ultima.controller.pub;

import com.tinusj.ultima.dao.dto.ChatMessageDto;
import com.tinusj.ultima.dao.dto.ConversationDto;
import com.tinusj.ultima.dao.dto.UserSearchDto;
import com.tinusj.ultima.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of conversations retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @GetMapping("/conversations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ConversationDto>> getConversations() {
        return ResponseEntity.ok(chatService.getConversations());
    }

    @Operation(summary = "Start a new conversation", description = "Creates a new conversation with the specified user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conversation created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @PostMapping("/conversations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ConversationDto> startConversation(@RequestParam Long userId) {
        return ResponseEntity.ok(chatService.startConversation(userId));
    }

    @Operation(summary = "Get messages for a conversation", description = "Returns all messages in the specified conversation.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Messages retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Conversation not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @GetMapping("/conversations/{conversationId}/messages")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ChatMessageDto>> getMessages(@PathVariable Long conversationId) {
        return ResponseEntity.ok(chatService.getMessages(conversationId));
    }

    @Operation(summary = "Send a message in a conversation", description = "Sends a message in the specified conversation and broadcasts it via WebSocket.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Message sent successfully"),
            @ApiResponse(responseCode = "404", description = "Conversation not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @PostMapping("/conversations/{conversationId}/messages")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChatMessageDto> sendMessage(
            @PathVariable Long conversationId,
            @RequestBody ChatMessageDto messageDto) {
        return ResponseEntity.ok(chatService.sendMessage(conversationId, messageDto));
    }

    @Operation(summary = "Search users by username", description = "Returns a list of users matching the search query for starting a conversation.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @GetMapping("/users/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserSearchDto>> searchUsers(@RequestParam String query) {
        return ResponseEntity.ok(chatService.searchUsers(query));
    }
}