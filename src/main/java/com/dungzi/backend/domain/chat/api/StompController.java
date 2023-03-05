package com.dungzi.backend.domain.chat.api;

import com.dungzi.backend.domain.chat.application.ChatMessageService;
import com.dungzi.backend.domain.chat.application.StompService;
import com.dungzi.backend.domain.chat.domain.ChatMessage;
import com.dungzi.backend.domain.chat.dto.ChatMessageRequestDto;
import com.dungzi.backend.domain.chat.dto.ChatMessageResponseDto;
import com.dungzi.backend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@RequiredArgsConstructor
@Controller
public class StompController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final StompService stompService;

    @GetMapping("/test")
    public String test() {
        return "websocket.html";
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageRequestDto messageSendDto, Message message) {
        User sender = stompService.getUserFromHeader(
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class));

        ChatMessage chatMessage = chatMessageService.saveChatMessage(messageSendDto, sender);

        messagingTemplate.convertAndSend("/queue/" + messageSendDto.getChatRoomId().toString(),
                ChatMessageResponseDto.toChatMessageResponseDto(chatMessage));
    }


}
