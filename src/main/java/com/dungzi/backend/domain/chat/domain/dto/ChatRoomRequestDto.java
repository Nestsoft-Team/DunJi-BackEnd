package com.dungzi.backend.domain.chat.domain.dto;

import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.ChatRoomType;
import lombok.Data;

public class ChatRoomRequestDto {
    @Data
    public static class CreateChatRoom {
        private ChatRoomType chatRoomType;
        private String userNickName;
    }

}
