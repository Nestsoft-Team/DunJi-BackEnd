package com.dungzi.backend.domain.chat.dto;

import com.dungzi.backend.domain.chat.domain.ChatMessage;
import com.dungzi.backend.domain.chat.domain.ChatMessageType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageResponseDto {
    private String sender;
    private String content;
    private String sendDate;
    private String messageType;


    public static String changeDateFormat(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.M.dd a hh:mm");
        return localDateTime.format(formatter);
    }

    public static ChatMessageResponseDto toChatMessageResponseDto(ChatMessage chatMessage) {
        ChatMessageResponseDto messageReceiveDto = ChatMessageResponseDto.builder()
                .sendDate(ChatMessageResponseDto.changeDateFormat(chatMessage.getSendDate()))
                .content(chatMessage.getContent())
                .sender(chatMessage.getSender().getNickname())
                .messageType(ChatMessageType.MESSAGE.getType())
                .build();
        return messageReceiveDto;
    }
}
