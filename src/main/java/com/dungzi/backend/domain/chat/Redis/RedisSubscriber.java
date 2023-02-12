package com.dungzi.backend.domain.chat.Redis;

import com.dungzi.backend.domain.chat.domain.ChatMessage;
import com.dungzi.backend.domain.chat.domain.ChatMessageType;
import com.dungzi.backend.domain.chat.dto.ChatMessageResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener{
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 onMessage가 해당 메시지를 받아 처리한다.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // redis에서 발행된 데이터를 받아 deserialize
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            // ChatMessage 객채로 맵핑
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            // Websocket 구독자에게 채팅 메시지 Send
            ChatMessageResponseDto chatMessageResponseDto = ChatMessageResponseDto.builder()
                    .sendDate(ChatMessageResponseDto.changeDateFormat(chatMessage.getSendDate()))
                    .content(chatMessage.getContent())
                    .sender(chatMessage.getSender().getNickname())
                    .messageType(ChatMessageType.MESSAGE.getType())
                    .build();
            messagingTemplate.convertAndSend("/queue/"  + chatMessage.getChatRoom().getChatRoomId(), chatMessageResponseDto);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}