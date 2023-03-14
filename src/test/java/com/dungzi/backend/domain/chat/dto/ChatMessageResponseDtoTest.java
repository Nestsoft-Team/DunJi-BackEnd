package com.dungzi.backend.domain.chat.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.dungzi.backend.domain.chat.domain.ChatMessage;
import com.dungzi.backend.domain.chat.domain.ChatMessageType;
import com.dungzi.backend.domain.user.domain.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChatMessageResponseDtoTest {


    @Test
    @DisplayName("chatMessage를 responseDto로 변경")
    void testToChatMessageResponseDto() {
        //given
        String content="test";
        String userNickName = "testUser";
        ChatMessage chatMessage = ChatMessage.builder()
                .sendDate(LocalDateTime.now())
                .content(content)
                .sender(User.builder().nickname(userNickName).build())
                .chatMessageType(ChatMessageType.MESSAGE)
                .build();
        //when
        ChatMessageResponseDto result = ChatMessageResponseDto.toChatMessageResponseDto(chatMessage);
        //then
        assertEquals(result.getSender(), userNickName);
        assertEquals(result.getContent(), content);
        assertEquals(result.getMessageType(), ChatMessageType.MESSAGE.getType());
    }


}